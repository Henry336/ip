package ari;

import java.io.IOException;

import ari.exception.EmptyArgumentException;
import ari.exception.TaskNotFoundException;
import ari.storage.Storage;
import ari.task.Task;
import ari.task.TaskList;
import ari.ui.Ui;

/**
 * Runs the Ari task manager.
 */
public class Ari {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates Ari with the components needed to run the task manager.
     *
     * @param filePath Path of the task data file.
     */
    public Ari(String filePath) {
        this.storage = new Storage(filePath);
        this.tasks = new TaskList();
        this.ui = new Ui();
    }

    /**
     * Starts Ari and processes commands until the user exits.
     */
    public void run() {
        this.ui.showWelcome();
        loadTasks();

        while (true) {
            String input = this.ui.readCommand();
            CommandType command = Parser.parseCommandType(input);

            if (command.equals(CommandType.EXIT) || command.equals(CommandType.BYE)) {
                saveTasks();
                this.ui.showMessage(command.getDescription());
                break;
            }

            try {
                switch (command) {
                    case LIST:
                        this.ui.showMessage(command.getDescription());
                        this.ui.showMessage(this.tasks.toString());
                        break;

                    case FIND:
                        String keyword = Parser.parseFindKeyword(input);
                        TaskList matchingTasks = this.tasks.findMatchingTasks(keyword);
                        String matchingTasksText = (matchingTasks.getLength() == 0)
                                ? "No matching tasks found."
                                : matchingTasks.toString().stripTrailing();

                        this.ui.showMessageWithLines(String.format(
                                "%s\n%s",
                                command.getDescription(),
                                matchingTasksText
                        ));
                        break;

                    case MARK:
                    case UNMARK:
                        int index = Parser.parseTaskId(input);
                        String changedTask = (command.equals(CommandType.MARK))
                                ? this.tasks.markTask(index)
                                : this.tasks.unmarkTask(index);

                        this.ui.showMessageWithLines(String.format(
                                "%s\n %s",
                                command.getDescription(),
                                changedTask)
                        );
                        break;

                    case TODO:
                    case DEADLINE:
                    case EVENT:
                        Task addedTask = Parser.parseTask(input, command);
                        this.tasks.addTask(addedTask);
                        this.ui.showMessageWithLines(String.format(
                                "%s\n %s\n%s",
                                command.getDescription(),
                                addedTask,
                                this.tasks.getLengthText()
                        ));
                        break;

                    case DELETE:
                        int taskId = Parser.parseTaskId(input);
                        String startingText = command.getDescription();
                        String middleText = this.tasks.deleteTask(taskId);
                        String endingText = this.tasks.getLengthText();

                        if (middleText.equals("None")) {
                            startingText = "Fortunately, there was nothing to delete.";
                            middleText = "Because you've completed all your tasks!";
                            endingText = "Good job! Keep this up!";
                        }

                        this.ui.showMessageWithLines(String.format(
                                "%s\n %s\n%s",
                                startingText,
                                middleText,
                                endingText
                        ));
                        break;

                    case UNKNOWN:
                        this.ui.showMessage(
                                "Sorry, I didn't get that... Could you say something else? ^.^"
                        );
                        break;
                }
            } catch (EmptyArgumentException e) {
                this.ui.showMessage(e.getMessage());
            } catch (TaskNotFoundException e) {
                this.ui.showMessage(e.getMessage());
            } catch (NumberFormatException e) {
                this.ui.showMessage("Oops! You can only enter integer IDs. Try again!");
            }
        }

        this.ui.close();
    }

    private void loadTasks() {
        try {
            this.storage.start();
        } catch (IOException e) {
            this.ui.showStorageInitializationError(e.getMessage());
        }

        try {
            boolean isDataFilePresent = this.storage.loadInto(this.tasks);
            if (isDataFilePresent) {
                this.ui.showTasksLoaded();
            } else {
                this.ui.showNoSavedTasks();
            }
        } catch (IOException | IllegalArgumentException e) {
            this.ui.showLoadingError(e.getMessage());
        }
    }

    private void saveTasks() {
        try {
            this.storage.saveFrom(this.tasks);
            this.ui.showTasksSaved();
        } catch (IOException e) {
            this.ui.showSavingError(e.getMessage());
        }
    }

    /**
     * Creates and runs Ari using the default task data file.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Ari("data/ari.txt").run();
    }
}
