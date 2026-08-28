public class DeadlineTask extends Task {
    private String deadline = "";

    public DeadlineTask(String description, String deadline) {
        super(description, "D");
        this.deadline = deadline;
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
