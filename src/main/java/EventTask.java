public class EventTask extends Task {
    private String fromDay;
    private String fromTime;
    private String to;

    public EventTask(String description, String from, String to) {
        super(description, "E");
        this.fromDay = from.split(" ")[1];
        this.fromTime = from.split(" ")[2];
        this.to = to.split(" ")[1];
    }

    public String[] getEventTime() {
        return new String[]{this.fromDay, this.fromTime, this.to};
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
                "[%s]%s %s (from: %s %s to: %s)",
                this.type, box, this.description, this.fromDay, this.fromTime, this.to);
    }
}
