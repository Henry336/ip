import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages the user's tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Adds the specified task to the task list.
     *
     * @param task Task to add.
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public String markTask(int id) throws TaskNotFoundException {
        if (id <= 0 || id - 1 >= this.tasks.size()) {
            throw new TaskNotFoundException(id);
        }

        Task task = this.tasks.get(id - 1);
        task.markTask();
        return task.toString();
    }

    public String unmarkTask(int id) throws TaskNotFoundException {
        if (id <= 0 || id - 1 >= this.tasks.size()) {
            throw new TaskNotFoundException(id);
        }

        Task task = this.tasks.get(id - 1);
        task.unmarkTask();
        return task.toString();
    }

    public String deleteTask(int id) throws TaskNotFoundException {
        if (this.tasks.isEmpty()) {
            return "None"; // if no tasks remain, then it's fine
        } else if (id <= 0 || id - 1 >= this.tasks.size()) {
            throw new TaskNotFoundException(id); // if there is/are task(s), then we must throw an exception
        }

        Task deletedTask = this.tasks.get(id - 1);
        this.tasks.remove(id - 1);

        return deletedTask.toString();
    }

    public List<Task> getAllTasks() {
        return List.copyOf(this.tasks);
    }

    public Task getTask(int id) {
        return this.tasks.get(id);
    }

    public int getLength() {
        return this.tasks.size();
    }

    public String getLengthText() {
        String taskNounForm = (this.tasks.size() == 1)
                ? "task"
                : "tasks";

        if (this.tasks.isEmpty()) {
            return "You currently have no tasks remaining. Good job!";
        }

        return String.format(
                "You currently have %s %s in the list! Better get working...",
                this.tasks.size(),
                taskNounForm
        );
    }

    @Override
    public String toString() {
        if (this.tasks.isEmpty()) {
            return this.getLengthText();
        }

        int num = 1;
        String result = "";

        for (Task task : this.tasks) {
            result += String.format("%s. %s\n", num, task.toString());
            num += 1;
        }

        return result;
    }
}
