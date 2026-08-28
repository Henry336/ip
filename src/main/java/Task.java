public class Task {
    protected String description;
    protected boolean isDone;
    protected String type;

    public Task(String rawDescription, String type) {
        String[] descArr = rawDescription.split(" ");
        String desc = "";

        int idx = 1;
        while (idx < descArr.length) {
            desc += descArr[idx] + " ";
            idx += 1;
        }

        this.description = desc.strip();
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
