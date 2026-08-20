public class TaskList {
    private Task[] tasks;
    private int idx;

    public TaskList(int size) {
        this.tasks = new Task[size];
        this.idx = 0;
    }

    public String addTask(String description) {
        this.tasks[this.idx] = new Task(description);
        this.idx += 1;
        return description;
    }

    public String changeTaskState(int id) {
        Task task = this.tasks[id - 1];
        task.changeState();
        return task.toString();
    }

    public void printTasks(Task[] list) {
        int init = 0;
        String result = "";

        while (init < idx) {
            result += String.format("%s. %s\n", init + 1, this.tasks[init]);
            init += 1;
        }

        System.out.println(result);
    }

    public Task getTask(int idx) {
        if (idx > this.idx) {
            return null;
        } else {
            return tasks[idx];
        }
    }

    @Override
    public String toString() {
        int i = 0;
        String result = "";

        while (i < this.idx) {
            result += String.format("%s. %s\n", i + 1, this.tasks[i].toString());
            i += 1;
        }

        return result;
    }
}
