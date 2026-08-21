import java.util.Collections;
import java.util.ArrayList;

public class TaskArray {
    private ArrayList<Task> tasks;

    public TaskArray() {
        tasks = new ArrayList<>();
    }

    public String addTask(String description, String type) throws EmptyArgumentException {
        String[] taskArr = description.strip().split("/");
        String[] cmdArr = taskArr[0].split(" ");
        if (cmdArr.length < 2) {
            throw new EmptyArgumentException(cmdArr[0]);
        }

        Task taskToAdd = new TodoTask(description);

        switch (type) {
            case "T":
                break;
            case "D":
                String[] taskArr1 = description.strip().split("/by"); // [deadline return book, by Sunday)
                String taskName1 = taskArr1[0].strip();
                String deadline = taskArr1[1].strip();

                taskToAdd = new DeadlineTask(taskName1, deadline);
                break;
            case "E":
                String[] taskArr2 = description.strip().split("/from");
                String taskName2 = taskArr2[0].strip();

                String[] fromToArr = taskArr2[1].split("/to");
                String from = fromToArr[0].strip();
                String to = fromToArr[1].strip();

                taskToAdd = new EventTask(taskName2, from, to);
                break;
        }

        this.tasks.add(taskToAdd);
        return taskToAdd.toString();
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

    public Task getTask(int id) { return this.tasks.get(id); }

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

        return String.format("You currently have %s %s in the list! Better get working...", this.tasks.size(), taskNounForm);
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