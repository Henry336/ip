import java.io.IOException;

/**
 * Runs the Ari task manager.
 */
public class Ari {
    public static void main(String[] args) {
        Ui ui = new Ui();
        TaskList taskList = new TaskList();
        Storage storage = new Storage("data/ari.txt");

        ui.showWelcome();

        // Initialize the storage
        // Load any data from pre-existing file
        try {
            storage.start();
        } catch (IOException e) {
            ui.showStorageInitializationError(e.getMessage());
        }

        try {
            boolean isDataFilePresent = storage.loadInto(taskList);
            if (isDataFilePresent) {
                ui.showTasksLoaded();
            } else {
                ui.showNoSavedTasks();
            }
        } catch (IOException | IllegalArgumentException e) {
            ui.showLoadingError(e.getMessage());
        }

        // Input loop
        while (true) {
            String input = ui.readCommand();
            CommandType command = Parser.parseCommandType(input);

            // Save the modified data to the specified path and exit the program
            if (command.equals(CommandType.EXIT) || command.equals(CommandType.BYE)) {
                try {
                    storage.saveFrom(taskList);
                    ui.showTasksSaved();
                } catch (IOException e) {
                    ui.showSavingError(e.getMessage());
                }
                ui.showMessage(command.getDescription());
                break;
            }

            try {
                switch (command) {
                    case LIST:
                        ui.showMessage(command.getDescription());
                        ui.showMessage(taskList.toString());
                        break;

                    case MARK:
                    case UNMARK:
                        int idx = Parser.parseTaskId(input);
                        String changedTask = (command.equals(CommandType.MARK))
                                ? taskList.markTask(idx)
                                : taskList.unmarkTask(idx);

                        ui.showMessageWithLines(String.format(
                                "%s\n %s",
                                command.getDescription(),
                                changedTask)
                        );
                        break;

                    case TODO:
                    case DEADLINE:
                    case EVENT:
                        Task addedTask = Parser.parseTask(input, command);
                        taskList.addTask(addedTask);
                        ui.showMessageWithLines(String.format(
                                "%s\n %s\n%s",
                                command.getDescription(),
                                addedTask,
                                taskList.getLengthText()
                        ));
                        break;

                    case DELETE:
                        int taskId = Parser.parseTaskId(input);
                        String startingText = command.getDescription();
                        String middleText = taskList.deleteTask(taskId);
                        String endingText = taskList.getLengthText();

                        if (middleText.equals("None")) {
                            startingText = "Fortunately, there was nothing to delete.";
                            middleText = "Because you've completed all your tasks!";
                            endingText = "Good job! Keep this up!";
                        }

                        ui.showMessageWithLines(String.format(
                                "%s\n %s\n%s",
                                startingText,
                                middleText,
                                endingText
                        ));
                        break;

                    case UNKNOWN:
                        ui.showMessage("Sorry, I didn't get that... Could you say something else? ^.^");
                        break;
                }
            } catch (EmptyArgumentException e) {
                ui.showMessage(e.getMessage());
            } catch (TaskNotFoundException e) {
                ui.showMessage(e.getMessage());
            } catch (NumberFormatException e) {
                ui.showMessage("Oops! You can only enter integer IDs. Try again!");
            }
        }

        ui.close();
    }
}
