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

## TC-001 — Unknown command and empty task list

### Aim

Verify that an unknown command is rejected, an empty task list can be displayed, and `bye` exits cleanly.

### Preconditions

None. Start with a fresh program process.

### Input

```text
nonsense
list
bye
```

### Expected output

```text
   ----   
  / /\ \ 
 / /__\ \ 
/ /    \ \ 

Hola, I'm Ari!
Need any help?

Sorry, I didn't get that... Could you say something else? ^.^"
Here are the tasks on your list:

Bye Bye. See you again!
```

## TC-002 — Todo task lifecycle

### Aim

Verify adding a todo task, listing it, marking it complete, unmarking it, and exiting.

### Preconditions

None. Start with a fresh program process.

### Input

```text
todo read book
list
mark 1
unmark 1
bye
```

### Expected output

```text
   ----   
  / /\ \ 
 / /__\ \ 
/ /    \ \ 

Hola, I'm Ari!
Need any help?

____________________________________________________________
Gotcha. I've added this task for you:
 [T][ ] read book
You currently have 1 in the list! Better get working...
____________________________________________________________

Here are the tasks on your list:
1. [T][ ] read book

____________________________________________________________
Good job! I've marked this task as done:
 [T][X] read book
____________________________________________________________

____________________________________________________________
Gotcha, I've unmarked this task:
 [T][ ] read book
____________________________________________________________

Bye Bye. See you again!
```

## TC-003 — Deadline and event formatting

### Aim

Verify adding deadline and event tasks, retaining their time information, listing them in order, and exiting.

### Preconditions

None. Start with a fresh program process.

### Input

```text
deadline return book /by Sunday
event project meeting /from Monday 2pm /to 4pm
list
bye
```

### Expected output

```text
   ----   
  / /\ \ 
 / /__\ \ 
/ /    \ \ 

Hola, I'm Ari!
Need any help?

____________________________________________________________
Gotcha. I've added this task for you:
 [D][ ] return book (by: Sunday)
You currently have 1 in the list! Better get working...
____________________________________________________________

____________________________________________________________
Gotcha. I've added this task for you:
 [E][ ] project meeting (from: Monday 2pm to: 4pm)
You currently have 2 in the list! Better get working...
____________________________________________________________

Here are the tasks on your list:
1. [D][ ] return book (by: Sunday)
2. [E][ ] project meeting (from: Monday 2pm to: 4pm)

Bye Bye. See you again!
```
