/**
 * Represents a task that must be completed by a deadline.
 */
public class DeadlineTask extends Task {
    private final String deadline;

    /**
     * Creates a deadline task from a deadline command.
     *
     * @param command Full deadline command entered by the user.
     * @throws EmptyArgumentException If the description or deadline is empty.
     */
    public DeadlineTask(String command) throws EmptyArgumentException {
        this(parseCommand(command));
    }

    private DeadlineTask(String[] commandParts) {
        this(commandParts[0], commandParts[1]);
    }

    /**
     * Creates a deadline task from separately parsed storage fields.
     *
     * @param description Deadline task description prefixed by its command word.
     * @param deadline Deadline of the task.
     */
    public DeadlineTask(String description, String deadline) {
        super(description, "D");
        this.deadline = deadline;
    }

    private static String[] parseCommand(String command) throws EmptyArgumentException {
        String[] commandParts = command.strip().split("\\s+/by\\s+", 2);
        if (commandParts.length < 2 || commandParts[1].isBlank()) {
            throw new EmptyArgumentException("deadline");
        }

        String[] descriptionParts = commandParts[0].split("\\s+", 2);
        if (descriptionParts.length < 2 || descriptionParts[1].isBlank()) {
            throw new EmptyArgumentException("deadline");
        }

        return new String[] {commandParts[0].strip(), commandParts[1].strip()};
    }

    public String getDeadline() {
        return this.deadline;
    }

    @Override
    public String toDataString() {
        String status = this.isDone
                ? "1"
                : "0";

        return String.format(
                "%s | %s | %s (deadline: %s)",
                this.type,
                status,
                this.description,
                this.deadline
        );
    }

    @Override
    public String toString() {
        String box;

        if (this.isDone) {
            box = "[X]";
        } else {
            box = "[ ]";
        }

        return String.format(
                "[%s]%s %s (by: %s)",
                this.type, box, this.description, this.deadline);
    }
}
