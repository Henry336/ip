public class EmptyArgumentException extends Exception {
    private String description;
    public EmptyArgumentException(String taskType) {
        super(String.format("Oh no! You can't have an empty description for %ss", taskType));
        this.description = String.format("Oh no! You can't have an empty description for %ss", taskType);
    }

    public String getDesc() {
        return this.description;
    }
}
