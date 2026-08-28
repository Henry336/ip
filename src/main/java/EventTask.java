public class EventTask extends Task {
    private String from;
    private String to;

    public EventTask(String description, String from, String to) {
        super(description, "E");
        this.from = from;
        this.to = to;
    }

    public String[] getEventTime() {
        return new String[]{this.from, this.to};
    }

    @Override
    public String toDataString() {
        String status = isDone
                ? "1"
                : "0";

        return String.format(
                "%s | %s | %s (from: %s | to: %s)",
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
