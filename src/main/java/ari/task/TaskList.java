package ari.task;

import java.util.ArrayList;
import java.util.List;

import ari.exception.TaskNotFoundException;

/**
 * Stores and manages the user's tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
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

    /**
     * Marks the task with the specified task ID as completed.
     *
     * @param id One-based ID of the task to mark.
     * @return User-readable representation of the marked task.
     * @throws TaskNotFoundException If the task ID is not in the list.
     */
    public String markTask(int id) throws TaskNotFoundException {
        if (id <= 0 || id - 1 >= this.tasks.size()) {
            throw new TaskNotFoundException(id);
        }

        Task task = this.tasks.get(id - 1);
        task.markTask();
        return task.toString();
    }

    /**
     * Marks the task with the specified task ID as incomplete.
     *
     * @param id One-based ID of the task to unmark.
     * @return User-readable representation of the unmarked task.
     * @throws TaskNotFoundException If the task ID is not in the list.
     */
    public String unmarkTask(int id) throws TaskNotFoundException {
        if (id <= 0 || id - 1 >= this.tasks.size()) {
            throw new TaskNotFoundException(id);
        }

        Task task = this.tasks.get(id - 1);
        task.unmarkTask();
        return task.toString();
    }

    /**
     * Removes the task with the specified task ID.
     *
     * @param id One-based ID of the task to delete.
     * @return User-readable representation of the deleted task, or {@code None} if the list is empty.
     * @throws TaskNotFoundException If the list is non-empty and the task ID is not in it.
     */
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

    /**
     * Returns an unmodifiable copy of all tasks in this list.
     *
     * @return Copy of all tasks in this list.
     */
    public List<Task> getAllTasks() {
        return List.copyOf(this.tasks);
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param id Zero-based index of the task.
     * @return Task at the specified index.
     */
    public Task getTask(int id) {
        return this.tasks.get(id);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return Number of tasks in this list.
     */
    public int getLength() {
        return this.tasks.size();
    }

    /**
     * Returns a message describing the number of tasks in this list.
     *
     * @return Message describing the task count.
     */
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

    /**
     * Returns the numbered tasks in a user-readable format.
     *
     * @return Numbered task list, or the empty-list message when there are no tasks.
     */
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
