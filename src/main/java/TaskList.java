public class TaskList {
    private Task[] tasks;
    private int idx;
    private int count = 0;

    public TaskList(int size) {
        this.tasks = new Task[size];
        this.idx = 0;
    }

    public String addTask(String description, String type) throws EmptyArgumentException {
        String[] taskArr = description.strip().split("/");
        String[] cmdArr = taskArr[0].split(" ");
        if (cmdArr.length < 2) {
            throw new EmptyArgumentException(cmdArr[0]);
        }

        switch (type) {
            case "T":
                this.tasks[this.idx] = new TodoTask(description);
                break;
            case "D":
                String[] taskArr1 = description.strip().split("/"); // [deadline return book, by Sunday)
                String taskName1 = taskArr1[0];
                String rawDeadline = taskArr1[1];

                this.tasks[this.idx] = new DeadlineTask(taskName1, rawDeadline);
                break;
            case "E":
                String[] taskArr2 = description.strip().split("/");
                String taskName2 = taskArr2[0];
                String from = taskArr2[1];
                String to = taskArr2[2];

                this.tasks[this.idx] = new EventTask(taskName2, from, to);
                break;
        }

        Task addedTask = this.tasks[this.idx];
        this.idx += 1;
        this.count += 1;
        return addedTask.toString();
    }

    public String markTask(int id) throws TaskNotFoundException {
        if (id - 1 >= this.idx) {
            throw new TaskNotFoundException(id);
        }

        Task task = this.tasks[id - 1];
        task.markTask();
        return task.toString();
    }

    public String unmarkTask(int id) throws TaskNotFoundException {
        if (id - 1 >= this.idx) {
            throw new TaskNotFoundException(id);
        }

        Task task = this.tasks[id - 1];
        task.unmarkTask();
        return task.toString();
    }

    public String deleteTask(int id) throws TaskNotFoundException {
        if (this.count == 0) {
            return "None"; // if no tasks remain, then it's fine
        } else if (id - 1 >= this.idx) {
            throw new TaskNotFoundException(id); // if there is/are task(s), then we must throw an exception
        }

        int i = id - 1;
        Task deletedTask = this.tasks[i];

        while (i < count && this.tasks[i] != null) {
            this.tasks[i] = this.tasks[i + 1];
            i += 1;
        }

        this.count -= 1;
        this.idx -= 1;

        return deletedTask.toString();
    }

    public Task getTask(int idx) {
        if (idx > this.idx) {
            return null;
        } else {
            return tasks[idx];
        }
    }

    public int getLength() {
        return this.count;
    }

    public String getLengthText() {
        String taskNounForm = (this.count == 1)
                ? "task"
                : "tasks";

        if (this.count == 0) {
            return "You currently have no tasks remaining. Good job!";
        }

        return String.format("You currently have %s %s in the list! Better get working...", this.count, taskNounForm);
    }

    @Override
    public String toString() {
        if (this.count == 0) {
            return this.getLengthText();
        }

        int i = 0;
        String result = "";

        while (i < this.idx) {
            result += String.format("%s. %s\n", i + 1, this.tasks[i].toString());
            i += 1;
        }

        return result;
    }
}
