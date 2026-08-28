/**
 * Represents a task without a deadline or scheduled time.
 */
public class TodoTask extends Task {
    /**
     * Creates a todo task from a todo command.
     *
     * @param command Full todo command entered by the user.
     * @throws EmptyArgumentException If the todo description is empty.
     */
    public TodoTask(String command) throws EmptyArgumentException {
        super(validateCommand(command), "T");
    }

    private static String validateCommand(String command) throws EmptyArgumentException {
        String[] commandParts = command.strip().split("\\s+", 2);
        if (commandParts.length < 2 || commandParts[1].isBlank()) {
            throw new EmptyArgumentException("todo");
        }

        return command.strip();
    }
}

