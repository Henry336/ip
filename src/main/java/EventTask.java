public class EventTask extends Task {
    private String from;
    private String to;

    public EventTask(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String[] getEventTime() {
        return new String[]{this.from, this.to};
    }
}
