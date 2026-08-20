public class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void changeState() {
        this.isDone = !this.isDone;
    }

    @Override
    public String toString() {
        String box = "";

        if (this.isDone) {
            box = "[X]";
        } else {
            box = "[ ]";
        }

        return box + " " + description;
    }
}
