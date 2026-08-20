---
name: test-ui
description: Run planned console UI test cases from test/ui-test-plan.md, compare actual output with expected output, stop at the first failure, and report the complete console transcript. Use when the user asks to test the application's text UI or supplies console commands and expected outputs.
---

# Test the Console UI

Use `test/ui-test-plan.md` as the source of truth for console UI tests.

## Record the test plan

When the user supplies new or revised test cases, update `test/ui-test-plan.md` before running them. Preserve unrelated existing cases. For every test case, record:

- a unique name or identifier;
- the aim of the test;
- any setup or preconditions;
- the ordered console input commands; and
- the complete expected console output, including startup and shutdown output.

Also keep the working directory, compile command, run command, Java version, session-isolation rule, and comparison rule current in the plan. Ask for clarification only when an omitted expected output or command makes the test impossible to evaluate.

## Run the tests

1. Work from the repository root containing `test/ui-test-plan.md`.
2. Read the whole plan and select the cases requested by the user. If no subset is requested, run all recorded cases in document order.
3. Confirm that the Java runtime used by the compile and run commands is JDK 25. Stop before testing and report the problem if it is not.
4. Compile once using the command in the plan. If compilation fails, stop and show the compiler command and output.
5. Start a fresh program process for each test case. Feed that case's input commands in their recorded order, including the command that exits the program when applicable.
6. Capture standard output and standard error without hiding, reordering, or trimming content. Treat a nonzero exit code or unexpected standard-error output as a failure unless the test explicitly expects it.
7. Normalize only line endings (`CRLF` and `LF`) before comparing. Otherwise compare the full actual output exactly, including blank lines, spaces, punctuation, prompts, startup text, and shutdown text.
8. After a passing case, continue to the next case. At the first failing case, terminate its process if it is still running and do not run any remaining cases.

Do not modify application code merely to make a test pass unless the user separately asks for a fix.

## Report the session

Always show the test cases in execution order and a console transcript for every case that ran. Clearly distinguish entered input from program output while preserving the actual text, for example by prefixing input lines with `> ` and placing the transcript in a fenced text block.

For each passing case, report `PASS`. If a case fails, report `FAIL`, identify the first mismatch when practical, and show both the complete expected output and complete actual output in separate fenced text blocks. State that the remaining cases were not run because testing stops on the first failure.
