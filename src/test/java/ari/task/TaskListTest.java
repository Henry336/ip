package ari.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ari.exception.TaskNotFoundException;

import org.junit.jupiter.api.Test;

public class TaskListTest {
    @Test
    public void markTask_validSecondId_marksSecondTaskOnly() throws TaskNotFoundException {
        TaskList tasks = new TaskList();
        tasks.addTask(new TodoTask("first"));
        tasks.addTask(new TodoTask("second"));

        String markedTask = tasks.markTask(2);

        assertEquals("[T][X] second", markedTask);
        assertEquals("[T][ ] first", tasks.getTask(0).toString());
        assertEquals("[T][X] second", tasks.getTask(1).toString());
    }

    @Test
    public void markTask_zeroId_throwsTaskNotFoundException() {
        TaskList tasks = createListWithOneTask();

        assertMarkTaskFails(tasks, 0);
    }

    @Test
    public void markTask_negativeId_throwsTaskNotFoundException() {
        TaskList tasks = createListWithOneTask();

        assertMarkTaskFails(tasks, -1);
    }

    @Test
    public void markTask_idBeyondListSize_throwsTaskNotFoundException() {
        TaskList tasks = createListWithOneTask();

        assertMarkTaskFails(tasks, 2);
    }

    @Test
    public void markTask_emptyList_throwsTaskNotFoundException() {
        TaskList tasks = new TaskList();

        assertMarkTaskFails(tasks, 1);
    }

    @Test
    public void findMatchingTasks_mixedCaseKeyword_returnsMatchesInOriginalOrder() {
        TaskList tasks = new TaskList();
        tasks.addTask(new TodoTask("read book"));
        tasks.addTask(new DeadlineTask("return BOOK", "Sunday"));
        tasks.addTask(new TodoTask("write code"));

        TaskList matches = tasks.findMatchingTasks("Book");

        assertEquals(2, matches.getLength());
        assertEquals("[T][ ] read book", matches.getTask(0).toString());
        assertEquals("[D][ ] return BOOK (by: Sunday)", matches.getTask(1).toString());
    }

    @Test
    public void findMatchingTasks_noMatch_returnsEmptyMutableTaskList() {
        TaskList tasks = createListWithOneTask();

        TaskList matches = tasks.findMatchingTasks("missing");
        matches.addTask(new TodoTask("later addition"));

        assertEquals(1, matches.getLength());
        assertEquals("[T][ ] later addition", matches.getTask(0).toString());
    }

    private TaskList createListWithOneTask() {
        TaskList tasks = new TaskList();
        tasks.addTask(new TodoTask("only task"));
        return tasks;
    }

    private void assertMarkTaskFails(TaskList tasks, int id) {
        try {
            tasks.markTask(id);
            fail();
        } catch (TaskNotFoundException e) {
            String expectedMessage = String.format(
                    "Task %s does not exist!\nSend 'list' to see which tasks you have left!",
                    id
            );
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}
