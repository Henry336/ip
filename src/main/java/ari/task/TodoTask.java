package ari.task;

/**
 * Represents a task without a deadline or scheduled time.
 */
public class TodoTask extends Task {
    /**
     * Creates an incomplete todo task.
     *
     * @param description Todo description.
     */
    public TodoTask(String description) {
        super(description, "T");
    }
}
