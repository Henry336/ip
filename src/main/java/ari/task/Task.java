package ari.task;

/**
 * Defines state and behavior shared by all task types.
 */
public abstract class Task {
    protected final String description;
    protected boolean isDone;
    protected final String type;

    protected Task(String description, String type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    public void changeState() {
        this.isDone = !this.isDone;
    }

    public void markTask() {
        this.isDone = true;
    }

    public void unmarkTask() {
        this.isDone = false;
    }

    public String toDataString() {
        String status = this.isDone
                ? "1"
                : "0";

        return String.format(
                "%s | %s | %s",
                this.type,
                status,
                this.description
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

        return String.format("[%s]%s %s", this.type, box, this.description);
    }
}
