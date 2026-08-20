# Console UI Test Plan

## Test environment

- Working directory: repository root
- Required Java version: JDK 25
- Compile command: `javac -d out src/main/java/*.java`
- Run command: `java -cp out Ari`
- Session isolation: start a fresh program process for each test case
- Comparison: normalize CRLF/LF line endings, then compare the complete output exactly
- Failure policy: terminate immediately on the first failure and do not run later cases

## Test case format

Each test case must use the following structure:

1. A level-two heading containing a unique test case identifier and name.
2. An **Aim** section explaining the behavior being checked.
3. A **Preconditions** section, or `None` when no setup is required.
4. An **Input** section containing the ordered console commands in a fenced `text` block.
5. An **Expected output** section containing the complete console output in a fenced `text` block, including startup and shutdown output.

## Recorded test cases

No test cases have been supplied yet.
