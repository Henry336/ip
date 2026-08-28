/**
 * Represents a task that takes place between a start and end time.
 */
public class EventTask extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task from separately parsed fields.
     *
     * @param description Event description.
     * @param from Start time of the event.
     * @param to End time of the event.
     */
    public EventTask(String description, String from, String to) {
        super(description, "E");
        this.from = requireNonBlank(from, "Event start");
        this.to = requireNonBlank(to, "Event end");
    }

    public String[] getEventTime() {
        return new String[] {this.from, this.to};
    }

    @Override
    public String toDataString() {
        String status = isDone
                ? "1"
                : "0";

        return String.format(
                "%s | %s | %s | %s | %s",
                this.type,
                status,
                this.description,
                this.from,
                this.to
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
                "[%s]%s %s (from: %s to: %s)",
                this.type, box, this.description, this.from, this.to);
    }
}
