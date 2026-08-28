package ari;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import ari.exception.EmptyArgumentException;
import ari.task.DeadlineTask;
import ari.task.EventTask;
import ari.task.Task;
import ari.task.TodoTask;

/**
 * Converts raw user input and saved records into domain values.
 */
public class Parser {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");

    /**
     * Identifies the command represented by the user's input.
     *
     * @param input Raw user input.
     * @return Matching command type, or UNKNOWN when none matches.
     */
    public static CommandType parseCommandType(String input) {
        if (input == null || input.isBlank()) {
            return CommandType.UNKNOWN;
        }

        String keyword = input.strip().split("\\s+", 2)[0];
        try {
            return CommandType.valueOf(keyword.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CommandType.UNKNOWN;
        }
    }

    /**
     * Creates the task described by a task-creation command.
     *
     * @param input Raw user input.
     * @param commandType Type of task-creation command.
     * @return Task represented by the command.
     * @throws EmptyArgumentException If a required task field is empty.
     */
    public static Task parseTask(String input, CommandType commandType)
            throws EmptyArgumentException {
        switch (commandType) {
            case TODO:
                return new TodoTask(parseTodoDescription(input));
            case DEADLINE:
                return parseDeadlineTask(input);
            case EVENT:
                return parseEventTask(input);
            default:
                throw new IllegalArgumentException("Command does not create a task");
            }
    }

    /**
     * Extracts a task ID from a command.
     *
     * @param input Raw user input.
     * @return Parsed task ID.
     * @throws NumberFormatException If the command does not contain an integer ID.
     */
    public static int parseTaskId(String input) {
        String[] commandParts = input.strip().split("\\s+");
        if (commandParts.length < 2) {
            throw new NumberFormatException("Missing task ID");
        }
        return Integer.parseInt(commandParts[1]);
    }

    /**
     * Reconstructs a task from one saved data record.
     *
     * @param line Saved data record.
     * @return Reconstructed task.
     * @throws IllegalArgumentException If the record is invalid.
     */
    public static Task parseStoredTask(String line) {
        String[] fields = line.split("\\s*\\|\\s*");
        if (fields.length < 3) {
            throw new IllegalArgumentException("Invalid saved task: " + line);
        }

        String status = fields[1];
        if (!status.equals("0") && !status.equals("1")) {
            throw new IllegalArgumentException("Invalid saved task status: " + status);
        }

        Task task;
        switch (fields[0]) {
            case "T":
                if (fields.length != 3) {
                    throw new IllegalArgumentException("Invalid saved task: " + line);
                }
                task = new TodoTask(fields[2]);
                break;
            case "D":
                if (fields.length != 4) {
                    throw new IllegalArgumentException("Invalid saved task: " + line);
                }
                task = new DeadlineTask(fields[2], fields[3]);
                break;
            case "E":
                if (fields.length != 5) {
                    throw new IllegalArgumentException("Invalid saved task: " + line);
                }
                task = new EventTask(fields[2], fields[3], fields[4]);
                break;
            default:
                throw new IllegalArgumentException("Unknown saved task type: " + fields[0]);
        }

        if (status.equals("1")) {
            task.markTask();
        }
        return task;
    }

    private static String parseTodoDescription(String input) throws EmptyArgumentException {
        String[] commandParts = input.strip().split("\\s+", 2);
        if (commandParts.length < 2 || commandParts[1].isBlank()) {
            throw new EmptyArgumentException("todo");
        }
        return commandParts[1].strip();
    }

    private static DeadlineTask parseDeadlineTask(String input)
            throws EmptyArgumentException {
        String[] commandParts = input.strip().split("\\s+/by\\s+", 2);
        if (commandParts.length < 2 || commandParts[1].isBlank()) {
            throw new EmptyArgumentException("deadline");
        }

        String[] descriptionParts = commandParts[0].split("\\s+", 2);
        if (descriptionParts.length < 2 || descriptionParts[1].isBlank()) {
            throw new EmptyArgumentException("deadline");
        }
        return new DeadlineTask(
                descriptionParts[1].strip(),
                commandParts[1].strip()
        );
    }

    private static EventTask parseEventTask(String input) throws EmptyArgumentException {
        String[] commandParts = input.strip().split("\\s+/from\\s+", 2);
        if (commandParts.length < 2) {
            throw new EmptyArgumentException("event");
        }

        String[] descriptionParts = commandParts[0].split("\\s+", 2);
        String[] timeParts = commandParts[1].split("\\s+/to\\s+", 2);
        boolean hasDescription = descriptionParts.length == 2 && !descriptionParts[1].isBlank();
        boolean hasTimes = timeParts.length == 2
                && !timeParts[0].isBlank()
                && !timeParts[1].isBlank();
        if (!hasDescription || !hasTimes) {
            throw new EmptyArgumentException("event");
        }

        return new EventTask(
                descriptionParts[1].strip(),
                timeParts[0].strip(),
                timeParts[1].strip()
        );
    }

    /**
     * Converts input string to a datetime string if possible,
     * otherwise, return original string
     *
     * @param s
     * @return Respective datetime string or input string
     */
    public static String parseDateTime(String s) {
        try {
            LocalDate date = LocalDate.parse(s);
            return date.format(formatter);
        } catch (DateTimeParseException e) {
            return s;
        }
    }
}
