public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(int id) {
        super(String.format("Task %s does not exist!\nSend 'list' to see which tasks you have left!", id));
    }
}
