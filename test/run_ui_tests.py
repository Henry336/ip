"""Run the console UI cases recorded in test/ui-test-plan.md.

The runner uses only Python's standard library. It compiles the Java sources with
JDK 25, executes every test case in a fresh process, compares complete console
output, and replaces test/ui-test-results.md with the latest results.
"""

from __future__ import annotations

import argparse
from dataclasses import dataclass
from datetime import datetime
import os
from pathlib import Path
import re
import shutil
import subprocess
import sys


if hasattr(sys.stdout, "reconfigure"):
    sys.stdout.reconfigure(encoding="utf-8")


REPOSITORY = Path(__file__).resolve().parent.parent
PLAN_PATH = REPOSITORY / "test" / "ui-test-plan.md"
RESULTS_PATH = REPOSITORY / "test" / "ui-test-results.md"
SOURCE_DIRECTORY = REPOSITORY / "src" / "main" / "java"
OUTPUT_DIRECTORY = REPOSITORY / "out"


@dataclass(frozen=True)
class TestCase:
    """A console UI test parsed from the Markdown plan."""

    name: str
    aim: str
    input_text: str
    expected_output: str


@dataclass(frozen=True)
class TestResult:
    """The captured outcome of one test case."""

    case: TestCase
    actual_output: str
    standard_error: str
    exit_code: int
    passed: bool


def normalize_newlines(text: str) -> str:
    """Normalize platform line endings without changing other whitespace."""

    return text.replace("\r\n", "\n").replace("\r", "\n")


def parse_cases(plan_text: str) -> list[TestCase]:
    """Extract ordered test cases from the documented Markdown structure."""

    pattern = re.compile(
        r"^## (?P<name>TC-[^\n]+)\n"
        r".*?^### Aim\n\n(?P<aim>.*?)\n\n"
        r".*?^### Input\n\n```text\n(?P<input>.*?)\n```\n"
        r".*?^### Expected output\n\n```text\n(?P<expected>.*?)\n```",
        re.MULTILINE | re.DOTALL,
    )
    cases = []
    for match in pattern.finditer(normalize_newlines(plan_text)):
        cases.append(
            TestCase(
                name=match.group("name"),
                aim=match.group("aim"),
                input_text=match.group("input"),
                expected_output=match.group("expected") + "\n",
            )
        )
    return cases


def executable_name(name: str) -> str:
    """Return the platform-specific Java executable name."""

    return f"{name}.exe" if os.name == "nt" else name


def candidate_jdk_homes(explicit_home: str | None) -> list[Path]:
    """Return likely JDK homes in priority order without duplicates."""

    candidates: list[Path] = []
    if explicit_home:
        candidates.append(Path(explicit_home).expanduser())
    if os.environ.get("JAVA_HOME"):
        candidates.append(Path(os.environ["JAVA_HOME"]))

    path_java = shutil.which("java")
    if path_java:
        candidates.append(Path(path_java).resolve().parent.parent)

    candidates.extend(sorted((Path.home() / ".jdks").glob("*25*"), reverse=True))
    candidates.extend(sorted(Path("/usr/lib/jvm").glob("*25*"), reverse=True))
    candidates.extend(
        sorted(
            Path("/Library/Java/JavaVirtualMachines").glob("*25*.jdk/Contents/Home"),
            reverse=True,
        )
    )

    unique: list[Path] = []
    seen: set[str] = set()
    for candidate in candidates:
        key = str(candidate.resolve()) if candidate.exists() else str(candidate)
        if key not in seen:
            seen.add(key)
            unique.append(candidate)
    return unique


def find_jdk_25(explicit_home: str | None) -> tuple[Path, Path, str]:
    """Locate Java and javac executables belonging to a JDK 25 installation."""

    for home in candidate_jdk_homes(explicit_home):
        bin_directory = home if home.name.lower() == "bin" else home / "bin"
        java = bin_directory / executable_name("java")
        javac = bin_directory / executable_name("javac")
        if not java.is_file() or not javac.is_file():
            continue

        version_result = subprocess.run(
            [str(java), "-version"],
            text=True,
            capture_output=True,
            encoding="utf-8",
            errors="replace",
        )
        version_output = normalize_newlines(
            version_result.stdout + version_result.stderr
        )
        if version_result.returncode == 0 and re.search(
            r'(?:java|openjdk) version "25(?:\.|\")', version_output
        ):
            return java, javac, version_output

    raise RuntimeError(
        "JDK 25 was not found. Set JAVA_HOME or pass --jdk with the JDK home."
    )


def markdown_block(text: str) -> list[str]:
    """Format captured text as a Markdown text block."""

    content = normalize_newlines(text)
    return ["```text", content.rstrip("\n"), "```"]


def first_difference(expected: str, actual: str) -> str:
    """Describe the first differing output line."""

    expected_lines = expected.splitlines()
    actual_lines = actual.splitlines()
    for number, (expected_line, actual_line) in enumerate(
        zip(expected_lines, actual_lines), start=1
    ):
        if expected_line != actual_line:
            return (
                f"First mismatch at line {number}: expected `{expected_line}`, "
                f"actual `{actual_line}`."
            )
    if len(expected_lines) != len(actual_lines):
        return (
            "Output line count differs: "
            f"expected {len(expected_lines)}, actual {len(actual_lines)}."
        )
    return "Output text differs."


def write_results(
    java_version: str,
    compile_status: str,
    results: list[TestResult],
    compile_output: str = "",
) -> None:
    """Replace the durable latest-run Markdown report."""

    failures = [result for result in results if not result.passed]
    timestamp = datetime.now().astimezone().isoformat(timespec="seconds")
    java_summary = java_version.splitlines()[0] if java_version else "Unavailable"
    lines = [
        "# Latest Console UI Test Results",
        "",
        f"- Run at: {timestamp}",
        f"- Java: {java_summary}",
        f"- Compilation: {compile_status}",
        f"- Test cases run: {len(results)}",
        f"- Passed: {len(results) - len(failures)}",
        f"- Failed: {len(failures)}",
        "",
    ]

    if compile_output:
        lines.extend(["## Compiler output", "", *markdown_block(compile_output), ""])

    lines.extend(
        [
            "## Results in execution order",
            "",
            "| Test case | Result |",
            "| --- | --- |",
        ]
    )
    for result in results:
        status = "PASS" if result.passed else "FAIL"
        lines.append(f"| {result.case.name} | {status} |")

    for result in failures:
        lines.extend(
            [
                "",
                f"## Failure: {result.case.name}",
                "",
                "### Aim",
                "",
                result.case.aim,
                "",
                "### Input",
                "",
                "```text",
                *[f"> {line}" for line in result.case.input_text.split("\n")],
                "```",
                "",
                "### Difference",
                "",
                first_difference(
                    result.case.expected_output, result.actual_output
                ),
                "",
                "### Actual output",
                "",
                *markdown_block(result.actual_output),
                "",
                "### Standard error",
                "",
                *markdown_block(result.standard_error or "None"),
                "",
                f"- Exit code: `{result.exit_code}`",
                "",
                "### Expected output",
                "",
                *markdown_block(result.case.expected_output),
            ]
        )

    with RESULTS_PATH.open("w", encoding="utf-8", newline="\n") as results_file:
        results_file.write("\n".join(lines) + "\n")


def print_case(result: TestResult) -> None:
    """Print an inspectable transcript for one executed case."""

    print(f"CASE: {result.case.name}")
    print("INPUT")
    for line in result.case.input_text.split("\n"):
        print(f"> {line}")
    print("ACTUAL OUTPUT")
    print(result.actual_output, end="")
    if result.standard_error:
        print("STANDARD ERROR")
        print(result.standard_error, end="")
    print(f"EXIT CODE: {result.exit_code}")
    print(f"RESULT: {'PASS' if result.passed else 'FAIL'}")
    if not result.passed:
        print("EXPECTED OUTPUT")
        print(result.case.expected_output, end="")


def main() -> int:
    """Compile the application, run every planned case, and save the report."""

    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--jdk",
        help="JDK 25 home or bin directory; normally discovered automatically",
    )
    args = parser.parse_args()

    cases = parse_cases(PLAN_PATH.read_text(encoding="utf-8"))
    if not cases:
        print("No test cases were found in test/ui-test-plan.md.", file=sys.stderr)
        return 2

    try:
        java, javac, java_version = find_jdk_25(args.jdk)
    except RuntimeError as error:
        print(error, file=sys.stderr)
        write_results("", "BLOCKED", [])
        return 2

    print("JDK VERSION")
    print(java_version, end="")

    sources = sorted(str(path) for path in SOURCE_DIRECTORY.glob("*.java"))
    compile_result = subprocess.run(
        [str(javac), "-d", str(OUTPUT_DIRECTORY), *sources],
        cwd=REPOSITORY,
        text=True,
        capture_output=True,
        encoding="utf-8",
        errors="replace",
    )
    compile_output = normalize_newlines(
        compile_result.stdout + compile_result.stderr
    )
    print("COMPILE")
    print(f"$ {javac} -d out src/main/java/*.java")
    if compile_output:
        print(compile_output, end="")
    if compile_result.returncode != 0:
        print(f"COMPILE FAIL (exit {compile_result.returncode})")
        write_results(java_version, "FAIL", [], compile_output)
        return 2
    print("COMPILE PASS")

    results: list[TestResult] = []
    for case in cases:
        process = subprocess.run(
            [str(java), "-cp", str(OUTPUT_DIRECTORY), "Ari"],
            cwd=REPOSITORY,
            input=case.input_text + "\n",
            text=True,
            capture_output=True,
            encoding="utf-8",
            errors="replace",
        )
        actual_output = normalize_newlines(process.stdout)
        standard_error = normalize_newlines(process.stderr)
        passed = (
            process.returncode == 0
            and not standard_error
            and actual_output == case.expected_output
        )
        result = TestResult(
            case=case,
            actual_output=actual_output,
            standard_error=standard_error,
            exit_code=process.returncode,
            passed=passed,
        )
        results.append(result)
        print_case(result)

    write_results(java_version, "PASS", results)
    failure_count = sum(not result.passed for result in results)
    print(
        f"SUMMARY: {len(results) - failure_count} passed, "
        f"{failure_count} failed"
    )
    return 1 if failure_count else 0


if __name__ == "__main__":
    raise SystemExit(main())
