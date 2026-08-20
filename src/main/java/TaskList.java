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

    public int getLength() {
        return this.count;
    }

    public String getLengthText() {
        return String.format("You currently have %s in the list! Better get working...", this.count);
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
