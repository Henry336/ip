/**
 * Represents a task that must be completed by a deadline.
 */
public class DeadlineTask extends Task {
    private final String deadline;

    /**
     * Creates a deadline task from separately parsed fields.
     *
     * @param description Deadline task description.
     * @param deadline Deadline of the task.
     */
    public DeadlineTask(String description, String deadline) {
        super(description, "D");
        this.deadline = requireNonBlank(deadline, "Deadline");
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
                "%s | %s | %s | %s",
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
