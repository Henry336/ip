# Console UI Test Plan

## Test environment

- Working directory: a fresh temporary directory for each test case
- Required Java version: JDK 25
- Test command: `python test/run_ui_tests.py`
- Compile command: `javac -d out src/main/java/*.java`
- Run command: `java -cp out Ari`
- Session isolation: start a fresh process with an independent `data/ari.txt`
- Comparison: normalize CRLF/LF line endings, then compare complete output and any expected data file exactly
- Failure policy: record each failure and continue running all remaining independent cases

## Test case format

Each test case must use the following structure:

1. A level-two heading containing a unique test case identifier and name.
2. An **Aim** section explaining the behavior being checked.
3. A **Preconditions** section, or `None` when no setup is required.
4. An **Input** section containing the ordered console commands in a fenced `text` block.
5. An **Expected output** section containing the complete console output in a fenced `text` block, including startup and shutdown output.
6. An optional **Initial data file** section for cases that begin with saved tasks.
7. An optional **Expected data file** section for cases that verify saved contents.

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

Here is a list of supported commands:

Keyword  |                 Format                | Description 

todo     | todo <task>                           | Adds a task to your list of tasks! (e.g., todo read book)
deadline | deadline <task> /by <time>            | Adds a task with a deadline. (e.g., deadline do something /by Sunday)
event    | event <event> /from <time> /to <time> | Adds an event with 'from' and 'to' times. (e.g., event dinner party /from Monday 2pm /to 9pm)
mark     | mark <task ID>                        | Marks the task with the task ID as done! (e.g., mark 1)
unmark   | unmark <task ID>                      | Does the opposite of mark. (e.g., unmark 1)
delete   | delete <task ID>                      | Removes the specified task from the list (e.g., delete 1)
list     | list                                  | Lists all your tasks in the order they were added in! (e.g., list)
exit     | exit                                  | Ends the program (e.g., exit)
bye      | bye                                   | Serves the same purpose as 'exit' (e.g., bye)

All tasks were loaded!
Sorry, I didn't get that... Could you say something else? ^.^
Here are the tasks on your list:
You currently have no tasks remaining. Good job!
I've saved your tasks.
Bye Bye. See you again!
```

## TC-013 — Reload every task type and completion state

### Aim

Verify that todo, deadline, and event tasks are reconstructed from storage without losing their fields or completion states.

### Preconditions

Start with the saved task records specified below.

### Initial data file

```text
T | 1 | read book
D | 0 | submit report | Friday 5pm
E | 1 | project meeting | Monday 2pm | Monday 4pm
```

### Input

```text
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

Here is a list of supported commands:

Keyword  |                 Format                | Description 

todo     | todo <task>                           | Adds a task to your list of tasks! (e.g., todo read book)
deadline | deadline <task> /by <time>            | Adds a task with a deadline. (e.g., deadline do something /by Sunday)
event    | event <event> /from <time> /to <time> | Adds an event with 'from' and 'to' times. (e.g., event dinner party /from Monday 2pm /to 9pm)
mark     | mark <task ID>                        | Marks the task with the task ID as done! (e.g., mark 1)
unmark   | unmark <task ID>                      | Does the opposite of mark. (e.g., unmark 1)
delete   | delete <task ID>                      | Removes the specified task from the list (e.g., delete 1)
list     | list                                  | Lists all your tasks in the order they were added in! (e.g., list)
exit     | exit                                  | Ends the program (e.g., exit)
bye      | bye                                   | Serves the same purpose as 'exit' (e.g., bye)

All tasks were loaded!
Here are the tasks on your list:
1. [T][X] read book
2. [D][ ] submit report (by: Friday 5pm)
3. [E][X] project meeting (from: Monday 2pm to: Monday 4pm)

I've saved your tasks.
Bye Bye. See you again!
```

### Expected data file

```text
T | 1 | read book
D | 0 | submit report | Friday 5pm
E | 1 | project meeting | Monday 2pm | Monday 4pm
```

## TC-014 — Reject corrupt saved data atomically

### Aim

Verify that an invalid saved status is reported without crashing, claiming success, or partially loading earlier records.

### Preconditions

Start with one valid record followed by a record containing an invalid status.

### Initial data file

```text
T | 0 | should not load
D | 2 | invalid status | Sunday
```

### Input

```text
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

Here is a list of supported commands:

Keyword  |                 Format                | Description 

todo     | todo <task>                           | Adds a task to your list of tasks! (e.g., todo read book)
deadline | deadline <task> /by <time>            | Adds a task with a deadline. (e.g., deadline do something /by Sunday)
event    | event <event> /from <time> /to <time> | Adds an event with 'from' and 'to' times. (e.g., event dinner party /from Monday 2pm /to 9pm)
mark     | mark <task ID>                        | Marks the task with the task ID as done! (e.g., mark 1)
unmark   | unmark <task ID>                      | Does the opposite of mark. (e.g., unmark 1)
delete   | delete <task ID>                      | Removes the specified task from the list (e.g., delete 1)
list     | list                                  | Lists all your tasks in the order they were added in! (e.g., list)
exit     | exit                                  | Ends the program (e.g., exit)
bye      | bye                                   | Serves the same purpose as 'exit' (e.g., bye)

Sorry, I couldn't load your tasks: Invalid saved task status: 2
Here are the tasks on your list:
You currently have no tasks remaining. Good job!
I've saved your tasks.
Bye Bye. See you again!
```

### Expected data file

```text

```

## TC-011 — Preserve a multiword deadline string

### Aim

Verify that Level-4 deadline input retains the complete free-form text following `/by`.

### Preconditions

None. Start with a fresh program process.

### Input

```text
deadline do homework /by no idea :-p
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

Here is a list of supported commands:

Keyword  |                 Format                | Description 

todo     | todo <task>                           | Adds a task to your list of tasks! (e.g., todo read book)
deadline | deadline <task> /by <time>            | Adds a task with a deadline. (e.g., deadline do something /by Sunday)
event    | event <event> /from <time> /to <time> | Adds an event with 'from' and 'to' times. (e.g., event dinner party /from Monday 2pm /to 9pm)
mark     | mark <task ID>                        | Marks the task with the task ID as done! (e.g., mark 1)
unmark   | unmark <task ID>                      | Does the opposite of mark. (e.g., unmark 1)
delete   | delete <task ID>                      | Removes the specified task from the list (e.g., delete 1)
list     | list                                  | Lists all your tasks in the order they were added in! (e.g., list)
exit     | exit                                  | Ends the program (e.g., exit)
bye      | bye                                   | Serves the same purpose as 'exit' (e.g., bye)

All tasks were loaded!
____________________________________________________________
Gotcha. I've added this task for you:
 [D][ ] do homework (by: no idea :-p)
You currently have 1 task in the list! Better get working...
____________________________________________________________

Here are the tasks on your list:
1. [D][ ] do homework (by: no idea :-p)

I've saved your tasks.
Bye Bye. See you again!
```

## TC-012 — Accept date-only event strings

### Aim

Verify that Level-4 event input accepts and retains arbitrary date strings without requiring time components.

### Preconditions

None. Start with a fresh program process.

### Input

```text
event orientation week /from 4/10/2019 /to 11/10/2019
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

Here is a list of supported commands:

Keyword  |                 Format                | Description 

todo     | todo <task>                           | Adds a task to your list of tasks! (e.g., todo read book)
deadline | deadline <task> /by <time>            | Adds a task with a deadline. (e.g., deadline do something /by Sunday)
event    | event <event> /from <time> /to <time> | Adds an event with 'from' and 'to' times. (e.g., event dinner party /from Monday 2pm /to 9pm)
mark     | mark <task ID>                        | Marks the task with the task ID as done! (e.g., mark 1)
unmark   | unmark <task ID>                      | Does the opposite of mark. (e.g., unmark 1)
delete   | delete <task ID>                      | Removes the specified task from the list (e.g., delete 1)
list     | list                                  | Lists all your tasks in the order they were added in! (e.g., list)
exit     | exit                                  | Ends the program (e.g., exit)
bye      | bye                                   | Serves the same purpose as 'exit' (e.g., bye)

All tasks were loaded!
____________________________________________________________
Gotcha. I've added this task for you:
 [E][ ] orientation week (from: 4/10/2019 to: 11/10/2019)
You currently have 1 task in the list! Better get working...
____________________________________________________________

Here are the tasks on your list:
1. [E][ ] orientation week (from: 4/10/2019 to: 11/10/2019)

I've saved your tasks.
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

Here is a list of supported commands:

Keyword  |                 Format                | Description 

todo     | todo <task>                           | Adds a task to your list of tasks! (e.g., todo read book)
deadline | deadline <task> /by <time>            | Adds a task with a deadline. (e.g., deadline do something /by Sunday)
event    | event <event> /from <time> /to <time> | Adds an event with 'from' and 'to' times. (e.g., event dinner party /from Monday 2pm /to 9pm)
mark     | mark <task ID>                        | Marks the task with the task ID as done! (e.g., mark 1)
unmark   | unmark <task ID>                      | Does the opposite of mark. (e.g., unmark 1)
delete   | delete <task ID>                      | Removes the specified task from the list (e.g., delete 1)
list     | list                                  | Lists all your tasks in the order they were added in! (e.g., list)
exit     | exit                                  | Ends the program (e.g., exit)
bye      | bye                                   | Serves the same purpose as 'exit' (e.g., bye)

All tasks were loaded!
____________________________________________________________
Gotcha. I've added this task for you:
 [T][ ] read book
You currently have 1 task in the list! Better get working...
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

I've saved your tasks.
Bye Bye. See you again!
```

### Expected data file

```text
T | 0 | read book
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

Here is a list of supported commands:

Keyword  |                 Format                | Description 

todo     | todo <task>                           | Adds a task to your list of tasks! (e.g., todo read book)
deadline | deadline <task> /by <time>            | Adds a task with a deadline. (e.g., deadline do something /by Sunday)
event    | event <event> /from <time> /to <time> | Adds an event with 'from' and 'to' times. (e.g., event dinner party /from Monday 2pm /to 9pm)
mark     | mark <task ID>                        | Marks the task with the task ID as done! (e.g., mark 1)
unmark   | unmark <task ID>                      | Does the opposite of mark. (e.g., unmark 1)
delete   | delete <task ID>                      | Removes the specified task from the list (e.g., delete 1)
list     | list                                  | Lists all your tasks in the order they were added in! (e.g., list)
exit     | exit                                  | Ends the program (e.g., exit)
bye      | bye                                   | Serves the same purpose as 'exit' (e.g., bye)

All tasks were loaded!
____________________________________________________________
Gotcha. I've added this task for you:
 [D][ ] return book (by: Sunday)
You currently have 1 task in the list! Better get working...
____________________________________________________________

____________________________________________________________
Gotcha. I've added this task for you:
 [E][ ] project meeting (from: Monday 2pm to: 4pm)
You currently have 2 tasks in the list! Better get working...
____________________________________________________________

Here are the tasks on your list:
1. [D][ ] return book (by: Sunday)
2. [E][ ] project meeting (from: Monday 2pm to: 4pm)

I've saved your tasks.
Bye Bye. See you again!
```

### Expected data file

```text
D | 0 | return book | Sunday
E | 0 | project meeting | Monday 2pm | 4pm
```

## TC-004 — Reject task ID zero

### Aim

Verify that task ID `0` is rejected with the normal task-not-found message and that the program remains usable afterward.

### Preconditions

None. Start with a fresh program process.

### Input

```text
mark 0
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

Here is a list of supported commands:

Keyword  |                 Format                | Description 

todo     | todo <task>                           | Adds a task to your list of tasks! (e.g., todo read book)
deadline | deadline <task> /by <time>            | Adds a task with a deadline. (e.g., deadline do something /by Sunday)
event    | event <event> /from <time> /to <time> | Adds an event with 'from' and 'to' times. (e.g., event dinner party /from Monday 2pm /to 9pm)
mark     | mark <task ID>                        | Marks the task with the task ID as done! (e.g., mark 1)
unmark   | unmark <task ID>                      | Does the opposite of mark. (e.g., unmark 1)
delete   | delete <task ID>                      | Removes the specified task from the list (e.g., delete 1)
list     | list                                  | Lists all your tasks in the order they were added in! (e.g., list)
exit     | exit                                  | Ends the program (e.g., exit)
bye      | bye                                   | Serves the same purpose as 'exit' (e.g., bye)

All tasks were loaded!
Task 0 does not exist!
Send 'list' to see which tasks you have left!
I've saved your tasks.
Bye Bye. See you again!
```

## TC-005 — Delete tasks

### Aim

Verify deleting an existing task and handling deletion when the task list is already empty.

### Preconditions

None. Start with a fresh program process.

### Input

```text
todo read book
delete 1
delete 1
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

Here is a list of supported commands:

Keyword  |                 Format                | Description 

todo     | todo <task>                           | Adds a task to your list of tasks! (e.g., todo read book)
deadline | deadline <task> /by <time>            | Adds a task with a deadline. (e.g., deadline do something /by Sunday)
event    | event <event> /from <time> /to <time> | Adds an event with 'from' and 'to' times. (e.g., event dinner party /from Monday 2pm /to 9pm)
mark     | mark <task ID>                        | Marks the task with the task ID as done! (e.g., mark 1)
unmark   | unmark <task ID>                      | Does the opposite of mark. (e.g., unmark 1)
delete   | delete <task ID>                      | Removes the specified task from the list (e.g., delete 1)
list     | list                                  | Lists all your tasks in the order they were added in! (e.g., list)
exit     | exit                                  | Ends the program (e.g., exit)
bye      | bye                                   | Serves the same purpose as 'exit' (e.g., bye)

All tasks were loaded!
____________________________________________________________
Gotcha. I've added this task for you:
 [T][ ] read book
You currently have 1 task in the list! Better get working...
____________________________________________________________

____________________________________________________________
Done! I've removed the task for you:
 [T][ ] read book
You currently have no tasks remaining. Good job!
____________________________________________________________

____________________________________________________________
Fortunately, there was nothing to delete.
 Because you've completed all your tasks!
Good job! Keep this up!
____________________________________________________________

I've saved your tasks.
Bye Bye. See you again!
```

## TC-006 — Recoverable input errors

### Aim

Verify that empty todo descriptions, non-integer IDs, and missing task IDs produce helpful messages without ending the session.

### Preconditions

None. Start with a fresh program process.

### Input

```text
todo
deadline
event
mark abc
mark 1
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

Here is a list of supported commands:

Keyword  |                 Format                | Description 

todo     | todo <task>                           | Adds a task to your list of tasks! (e.g., todo read book)
deadline | deadline <task> /by <time>            | Adds a task with a deadline. (e.g., deadline do something /by Sunday)
event    | event <event> /from <time> /to <time> | Adds an event with 'from' and 'to' times. (e.g., event dinner party /from Monday 2pm /to 9pm)
mark     | mark <task ID>                        | Marks the task with the task ID as done! (e.g., mark 1)
unmark   | unmark <task ID>                      | Does the opposite of mark. (e.g., unmark 1)
delete   | delete <task ID>                      | Removes the specified task from the list (e.g., delete 1)
list     | list                                  | Lists all your tasks in the order they were added in! (e.g., list)
exit     | exit                                  | Ends the program (e.g., exit)
bye      | bye                                   | Serves the same purpose as 'exit' (e.g., bye)

All tasks were loaded!
Oh no! You can't have an empty description for todos
Oh no! You can't have an empty description for deadlines
Oh no! You can't have an empty description for events
Oops! You can only enter integer IDs. Try again!
Task 1 does not exist!
Send 'list' to see which tasks you have left!
I've saved your tasks.
Bye Bye. See you again!
```

## TC-007 — Reject nonpositive IDs with an existing task

### Aim

Verify that mark, unmark, and delete reject zero or negative IDs without changing an existing task.

### Preconditions

None. Start with a fresh program process.

### Input

```text
todo keep me
unmark 0
mark -1
delete 0
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

Here is a list of supported commands:

Keyword  |                 Format                | Description 

todo     | todo <task>                           | Adds a task to your list of tasks! (e.g., todo read book)
deadline | deadline <task> /by <time>            | Adds a task with a deadline. (e.g., deadline do something /by Sunday)
event    | event <event> /from <time> /to <time> | Adds an event with 'from' and 'to' times. (e.g., event dinner party /from Monday 2pm /to 9pm)
mark     | mark <task ID>                        | Marks the task with the task ID as done! (e.g., mark 1)
unmark   | unmark <task ID>                      | Does the opposite of mark. (e.g., unmark 1)
delete   | delete <task ID>                      | Removes the specified task from the list (e.g., delete 1)
list     | list                                  | Lists all your tasks in the order they were added in! (e.g., list)
exit     | exit                                  | Ends the program (e.g., exit)
bye      | bye                                   | Serves the same purpose as 'exit' (e.g., bye)

All tasks were loaded!
____________________________________________________________
Gotcha. I've added this task for you:
 [T][ ] keep me
You currently have 1 task in the list! Better get working...
____________________________________________________________

Task 0 does not exist!
Send 'list' to see which tasks you have left!
Task -1 does not exist!
Send 'list' to see which tasks you have left!
Task 0 does not exist!
Send 'list' to see which tasks you have left!
Here are the tasks on your list:
1. [T][ ] keep me

I've saved your tasks.
Bye Bye. See you again!
```

## TC-008 — Delete from an empty list

### Aim

Verify that any delete attempt reports that there is nothing to delete when the task list is empty.

### Preconditions

None. Start with a fresh program process.

### Input

```text
delete 0
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

Here is a list of supported commands:

Keyword  |                 Format                | Description 

todo     | todo <task>                           | Adds a task to your list of tasks! (e.g., todo read book)
deadline | deadline <task> /by <time>            | Adds a task with a deadline. (e.g., deadline do something /by Sunday)
event    | event <event> /from <time> /to <time> | Adds an event with 'from' and 'to' times. (e.g., event dinner party /from Monday 2pm /to 9pm)
mark     | mark <task ID>                        | Marks the task with the task ID as done! (e.g., mark 1)
unmark   | unmark <task ID>                      | Does the opposite of mark. (e.g., unmark 1)
delete   | delete <task ID>                      | Removes the specified task from the list (e.g., delete 1)
list     | list                                  | Lists all your tasks in the order they were added in! (e.g., list)
exit     | exit                                  | Ends the program (e.g., exit)
bye      | bye                                   | Serves the same purpose as 'exit' (e.g., bye)

All tasks were loaded!
____________________________________________________________
Fortunately, there was nothing to delete.
 Because you've completed all your tasks!
Good job! Keep this up!
____________________________________________________________

I've saved your tasks.
Bye Bye. See you again!
```

## TC-009 — Reindex tasks after deletion

### Aim

Verify that deleting the first task shifts the second task to ID 1 and that the shifted task can be marked.

### Preconditions

None. Start with a fresh program process.

### Input

```text
todo first
todo second
delete 1
list
mark 1
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

Here is a list of supported commands:

Keyword  |                 Format                | Description 

todo     | todo <task>                           | Adds a task to your list of tasks! (e.g., todo read book)
deadline | deadline <task> /by <time>            | Adds a task with a deadline. (e.g., deadline do something /by Sunday)
event    | event <event> /from <time> /to <time> | Adds an event with 'from' and 'to' times. (e.g., event dinner party /from Monday 2pm /to 9pm)
mark     | mark <task ID>                        | Marks the task with the task ID as done! (e.g., mark 1)
unmark   | unmark <task ID>                      | Does the opposite of mark. (e.g., unmark 1)
delete   | delete <task ID>                      | Removes the specified task from the list (e.g., delete 1)
list     | list                                  | Lists all your tasks in the order they were added in! (e.g., list)
exit     | exit                                  | Ends the program (e.g., exit)
bye      | bye                                   | Serves the same purpose as 'exit' (e.g., bye)

All tasks were loaded!
____________________________________________________________
Gotcha. I've added this task for you:
 [T][ ] first
You currently have 1 task in the list! Better get working...
____________________________________________________________

____________________________________________________________
Gotcha. I've added this task for you:
 [T][ ] second
You currently have 2 tasks in the list! Better get working...
____________________________________________________________

____________________________________________________________
Done! I've removed the task for you:
 [T][ ] first
You currently have 1 task in the list! Better get working...
____________________________________________________________

Here are the tasks on your list:
1. [T][ ] second

____________________________________________________________
Good job! I've marked this task as done:
 [T][X] second
____________________________________________________________

I've saved your tasks.
Bye Bye. See you again!
```

## TC-010 — Accept surrounding and repeated whitespace

### Aim

Verify that leading whitespace, mixed command casing, and repeated spaces do not become part of a todo description.

### Preconditions

None. Start with a fresh program process.

### Input

```text
  ToDo   spaced task
  LIST
  BYE
```

### Expected output

```text
   ----   
  / /\ \ 
 / /__\ \ 
/ /    \ \ 

Hola, I'm Ari!
Need any help?

Here is a list of supported commands:

Keyword  |                 Format                | Description 

todo     | todo <task>                           | Adds a task to your list of tasks! (e.g., todo read book)
deadline | deadline <task> /by <time>            | Adds a task with a deadline. (e.g., deadline do something /by Sunday)
event    | event <event> /from <time> /to <time> | Adds an event with 'from' and 'to' times. (e.g., event dinner party /from Monday 2pm /to 9pm)
mark     | mark <task ID>                        | Marks the task with the task ID as done! (e.g., mark 1)
unmark   | unmark <task ID>                      | Does the opposite of mark. (e.g., unmark 1)
delete   | delete <task ID>                      | Removes the specified task from the list (e.g., delete 1)
list     | list                                  | Lists all your tasks in the order they were added in! (e.g., list)
exit     | exit                                  | Ends the program (e.g., exit)
bye      | bye                                   | Serves the same purpose as 'exit' (e.g., bye)

All tasks were loaded!
____________________________________________________________
Gotcha. I've added this task for you:
 [T][ ] spaced task
You currently have 1 task in the list! Better get working...
____________________________________________________________

Here are the tasks on your list:
1. [T][ ] spaced task

I've saved your tasks.
Bye Bye. See you again!
```
