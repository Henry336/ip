import java.util.Scanner;

/**
 * Handles input from and output to the user.
 */
public class Ui {
    private static final String DIVIDER =
            "____________________________________________________________";

    private static final String BANNER =
            "   ----   \n"
                    + "  / /\\ \\ \n"
                    + " / /__\\ \\ \n"
                    + "/ /    \\ \\ \n";

    private static final String COMMAND_LIST =
            "Here is a list of supported commands:\n\n"
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

    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the greeting and supported commands.
     */
    public void showWelcome() {
        showMessage(BANNER);
        showMessage("Hola, I'm Ari!\nNeed any help?\n");
        showMessage(COMMAND_LIST);
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return Full command entered by the user.
     */
    public String readCommand() {
        return this.scanner.nextLine();
    }

    /**
     * Displays a message to the user.
     *
     * @param message Message to display.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays a message between divider lines.
     *
     * @param message Message to display.
     */
    public void showMessageWithLines(String message) {
        showMessage(DIVIDER);
        showMessage(message);
        showMessage(DIVIDER + "\n");
    }

    /**
     * Closes the input scanner.
     */
    public void close() {
        this.scanner.close();
    }
}
