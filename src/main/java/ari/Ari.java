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
    private boolean hasStarted;

    /**
     * Creates Ari with the components needed to run the task manager.
     *
     * @param filePath Path of the task data file.
     */
    public Ari(String filePath) {
        this.storage = new Storage(filePath);
        this.tasks = new TaskList();
        this.ui = new Ui();
        this.hasStarted = false;
    }

    /**
     * Starts Ari and processes commands until the user exits.
     */
    public void run() {
        this.ui.showWelcome();
        this.ui.showMessage(start());

        while (true) {
            String input = this.ui.readCommand();
            CommandType command = Parser.parseCommandType(input);

            if (command.equals(CommandType.EXIT) || command.equals(CommandType.BYE)) {
                this.ui.showMessage(getExitResponse(command));
                break;
            }

            try {
                String response = executeCommand(input, command);
                if (shouldShowDivider(command)) {
                    this.ui.showMessageWithLines(response);
                } else {
                    this.ui.showMessage(response);
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

    /**
     * Initializes storage and loads saved tasks once.
     *
     * @return Message describing the result of loading saved tasks.
     */
    public String start() {
        if (this.hasStarted) {
            return "Ari is ready!";
        }
        this.hasStarted = true;

        try {
            this.storage.start();
        } catch (IOException e) {
            return "Sorry, I couldn't initialize the storage file: " + e.getMessage();
        }

        try {
            boolean isDataFilePresent = this.storage.loadInto(this.tasks);
            if (isDataFilePresent) {
                return "All tasks were loaded!";
            }
            return "There are no saved tasks yet!";
        } catch (IOException | IllegalArgumentException e) {
            return "Sorry, I couldn't load your tasks: " + e.getMessage();
        }
    }

    /**
     * Processes one user command and returns Ari's response.
     *
     * @param input Full command entered by the user.
     * @return Ari's response to the command.
     */
    public String getResponse(String input) {
        CommandType command = Parser.parseCommandType(input);
        if (command.equals(CommandType.EXIT) || command.equals(CommandType.BYE)) {
            return getExitResponse(command);
        }

        try {
            return executeCommand(input, command).stripTrailing();
        } catch (EmptyArgumentException | TaskNotFoundException e) {
            return e.getMessage();
        } catch (NumberFormatException e) {
            return "Oops! You can only enter integer IDs. Try again!";
        }
    }

    /**
     * Carries out a non-exit command against the task list.
     *
     * @param input Full command entered by the user.
     * @param command Parsed command type.
     * @return Response describing the command result.
     * @throws EmptyArgumentException If a required command argument is missing.
     * @throws TaskNotFoundException If a requested task does not exist.
     */
    private String executeCommand(String input, CommandType command)
            throws EmptyArgumentException, TaskNotFoundException {
        switch (command) {
            case LIST:
                return String.format(
                        "%s\n%s",
                        command.getDescription(),
                        this.tasks.toString()
                );

            case FIND:
                String keyword = Parser.parseFindKeyword(input);
                TaskList matchingTasks = this.tasks.findMatchingTasks(keyword);
                String matchingTasksText = (matchingTasks.getLength() == 0)
                        ? "No matching tasks found."
                        : matchingTasks.toString().stripTrailing();

                return String.format(
                        "%s\n%s",
                        command.getDescription(),
                        matchingTasksText
                );

            case MARK:
            case UNMARK:
                int index = Parser.parseTaskId(input);
                String changedTask = (command.equals(CommandType.MARK))
                        ? this.tasks.markTask(index)
                        : this.tasks.unmarkTask(index);

                return String.format(
                        "%s\n %s",
                        command.getDescription(),
                        changedTask
                );

            case TODO:
            case DEADLINE:
            case EVENT:
                Task addedTask = Parser.parseTask(input, command);
                this.tasks.addTask(addedTask);

                return String.format(
                        "%s\n %s\n%s",
                        command.getDescription(),
                        addedTask,
                        this.tasks.getLengthText()
                );

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

                return String.format(
                        "%s\n %s\n%s",
                        startingText,
                        middleText,
                        endingText
                );

            case UNKNOWN:
                return "Sorry, I didn't get that... Could you say something else? ^.^";

            default:
                throw new IllegalStateException("Unexpected command: " + command);
        }
    }

    /**
     * Returns whether console output for a command should be placed between dividers.
     *
     * @param command Parsed command type.
     * @return True for commands whose successful responses use dividers.
     */
    private boolean shouldShowDivider(CommandType command) {
        return command.equals(CommandType.FIND)
                || command.equals(CommandType.MARK)
                || command.equals(CommandType.UNMARK)
                || command.equals(CommandType.TODO)
                || command.equals(CommandType.DEADLINE)
                || command.equals(CommandType.EVENT)
                || command.equals(CommandType.DELETE);
    }

    /**
     * Saves all tasks and combines the save result with the exit message.
     *
     * @param command Exit command entered by the user.
     * @return Save result followed by the exit message.
     */
    private String getExitResponse(CommandType command) {
        return String.format("%s\n%s", saveTasks(), command.getDescription());
    }

    /**
     * Saves the current tasks and returns the result as a user-facing message.
     *
     * @return Message describing whether the tasks were saved.
     */
    private String saveTasks() {
        try {
            this.storage.saveFrom(this.tasks);
            return "I've saved your tasks.";
        } catch (IOException e) {
            return "Sorry, I couldn't save your tasks: " + e.getMessage();
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
