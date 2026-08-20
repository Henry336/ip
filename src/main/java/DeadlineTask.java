public class DeadlineTask extends Task {
    private String deadline;

    public DeadlineTask(String description, String rawDeadline) {
        super(description, "D");
        this.deadline = rawDeadline.split(" ")[1];
    }

    public String getDeadline() {
        return this.deadline;
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
