package ari.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        this.tasks = new ArrayList<>();
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
     * @param taskId One-based ID of the task to mark.
     * @return User-readable representation of the marked task.
     * @throws TaskNotFoundException If the task ID is not in the list.
     */
    public String markTask(int taskId) throws TaskNotFoundException {
        if (taskId <= 0 || taskId - 1 >= this.tasks.size()) {
            throw new TaskNotFoundException(taskId);
        }

        Task task = this.tasks.get(taskId - 1);
        task.markTask();
        return task.toString();
    }

    /**
     * Marks the task with the specified task ID as incomplete.
     *
     * @param taskId One-based ID of the task to unmark.
     * @return User-readable representation of the unmarked task.
     * @throws TaskNotFoundException If the task ID is not in the list.
     */
    public String unmarkTask(int taskId) throws TaskNotFoundException {
        if (taskId <= 0 || taskId - 1 >= this.tasks.size()) {
            throw new TaskNotFoundException(taskId);
        }

        Task task = this.tasks.get(taskId - 1);
        task.unmarkTask();
        return task.toString();
    }

    /**
     * Removes the task with the specified task ID.
     *
     * @param taskId One-based ID of the task to delete.
     * @return User-readable representation of the deleted task, or {@code None} if the list is empty.
     * @throws TaskNotFoundException If the list is non-empty and the task ID is not in it.
     */
    public String deleteTask(int taskId) throws TaskNotFoundException {
        if (this.tasks.isEmpty()) {
            return "None";
        }
        if (taskId <= 0 || taskId - 1 >= this.tasks.size()) {
            throw new TaskNotFoundException(taskId);
        }

        Task deletedTask = this.tasks.get(taskId - 1);
        this.tasks.remove(taskId - 1);

        return deletedTask.toString();
    }

    /**
     * Returns an unmodifiable copy of all tasks in this list.
     *
     * @return Copy of all tasks in this list.
     */
    /**
     * Returns the tasks whose descriptions contain the specified keyword.
     *
     * @param keyword Text to search for in task descriptions.
     * @return New task list containing the matching tasks in their original order.
     */
    public TaskList findMatchingTasks(String keyword) {
        TaskList matchingTasks = new TaskList();
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);

        for (Task task : this.tasks) {
            String normalizedDescription = task.description.toLowerCase(Locale.ROOT);
            if (normalizedDescription.contains(normalizedKeyword)) {
                matchingTasks.addTask(task);
            }
        }

        return matchingTasks;
    }

    public List<Task> getAllTasks() {
        return List.copyOf(this.tasks);
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index Zero-based index of the task.
     * @return Task at the specified index.
     */
    public Task getTask(int index) {
        return this.tasks.get(index);
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

        int taskNumber = 1;
        String taskListText = "";

        for (Task task : this.tasks) {
            taskListText += String.format("%s. %s\n", taskNumber, task);
            taskNumber += 1;
        }

        return taskListText;
    }
}
