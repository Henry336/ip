import java.util.Scanner;

public class Ari {
    public static String name = "Ari";
    public static int idx = 0;
    public static TaskArray tasks = new TaskArray();
    public static String listOfCmds = "Here is a list of supported commands:\n\n"
            + "Keyword  |                 Format                | Description \n\n"
            + "todo     | todo <task>                           | Adds a task to your list of tasks! (e.g., todo read book)\n"
            + "deadline | deadline <task> /by <time>            | Adds a task with a deadline. (e.g., deadline do something /by Sunday)\n"
            + "event    | event <event> /from <time> /to <time> | Adds an event with 'from' and 'to' times. (e.g., event dinner party /from Monday 2pm /to 9pm)\n"
            + "mark     | mark <task ID>                        | Marks the task with the task ID as done! (e.g., mark 1)\n"
            + "unmark   | unmark <task ID>                      | Does the opposite of mark. (e.g., unmark 1)\n"
            + "delete   | delete <task ID>                      | Removes the specified task from the list (e.g., delete 1)\n"
            + "list     | list                                  | Lists all your tasks in the order they were added in! (e.g., list)\n"
            + "exit     | exit                                  | Ends the program (e.g., exit)\n"
            + "bye      | bye                                   | Serves the same purpose as 'exit' (e.g., bye)\n";

    public static String banner =
            "   ----   \n"
                    + "  / /\\ \\ \n"
                    + " / /__\\ \\ \n"
                    + "/ /    \\ \\ \n";

    public static void print(String s) {
        System.out.println(s);
    }

    public static int getIdx(String s) {
        String result = s.strip().split("\\s+")[1];
        return Integer.parseInt(result);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        print(banner);
        print("Hola, I'm Ari!\n" + "Need any help?\n");
        print(listOfCmds);

        while (true) {
            String input = scanner.nextLine();
            CommandType command = CommandType.of(input);

            if (command.equals(CommandType.EXIT) || command.equals(CommandType.BYE)) {
                print(command.getDescription());
                break;
            }

            try {
                switch (command) {
                    case LIST:
                        print(command.getDescription());
                        print(tasks.toString());
                        break;

                    case MARK:
                    case UNMARK:
                        int idx = getIdx(input);
                        String changedTask = (command.equals(CommandType.MARK))
                                ? tasks.markTask(idx)
                                : tasks.unmarkTask(idx);

                        print(String.format(
                                "____________________________________________________________\n" +
                                        "%s\n %s\n" +
                                        "____________________________________________________________\n",
                                command.getDescription(),
                                changedTask)
                        );
                        break;

                    case TODO:
                        String addedTaskT = tasks.addTask(input, "T");
                        print(String.format(
                                "____________________________________________________________\n" +
                                        "%s\n %s\n%s\n" +
                                        "____________________________________________________________\n",
                                command.getDescription(),
                                addedTaskT.toString(),
                                tasks.getLengthText()
                        ));
                        break;

                    case DEADLINE:
                        String addedTaskD = tasks.addTask(input, "D");
                        print(String.format(
                                "____________________________________________________________\n" +
                                        "%s\n %s\n%s\n" +
                                        "____________________________________________________________\n",
                                command.getDescription(),
                                addedTaskD,
                                tasks.getLengthText()
                        ));
                        break;

                    case EVENT:
                        String addedTaskE = tasks.addTask(input, "E");
                        print(String.format(
                                "____________________________________________________________\n" +
                                        "%s\n %s\n%s\n" +
                                        "____________________________________________________________\n",
                                command.getDescription(),
                                addedTaskE,
                                tasks.getLengthText()
                        ));
                        break;

                    case DELETE:
                        int taskId = getIdx(input);
                        String startingText = command.getDescription();
                        String middleText = tasks.deleteTask(taskId);
                        String endingText = tasks.getLengthText();

                        if (middleText.equals("None")) {
                            startingText = "Fortunately, there was nothing to delete.";
                            middleText = "Because you've completed all your tasks!";
                            endingText = "Good job! Keep this up!";
                        }

                        print(String.format(
                                "____________________________________________________________\n" +
                                        "%s\n %s\n%s\n" +
                                        "____________________________________________________________\n",
                                startingText,
                                middleText,
                                endingText
                        ));
                        break;

                    case UNKNOWN:
                        print("Sorry, I didn't get that... Could you say something else? ^.^");
                        break;
                }
            } catch (EmptyArgumentException e) {
                print(e.getMessage());
            } catch (TaskNotFoundException e) {
                print(e.getMessage());
            } catch (NumberFormatException e) {
                print("Oops! You can only enter integer IDs. Try again!");
            }
        }

        scanner.close(); // always clean up
    }
}
