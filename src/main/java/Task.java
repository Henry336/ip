public class Task {
    private String description;
    private boolean isDone;
    private String type;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
        this.type = "T";
    }

    public void changeState() {
        this.isDone = !this.isDone;
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
