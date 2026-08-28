package ari.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import ari.Parser;
import ari.task.Task;
import ari.task.TaskList;

/**
 * Initializes the data file and handles task persistence.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a storage object that uses the specified file path.
     *
     * @param filePath Path of the task data file.
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Creates the data directory and file when they do not already exist.
     *
     * @throws IOException If the directory or file cannot be created.
     */
    public void start() throws IOException {
        Files.createDirectories(this.filePath.getParent());

        if (Files.notExists(this.filePath)) {
            Files.createFile(this.filePath);
        }
    }

    /**
     * Loads all valid saved tasks into the specified task list.
     *
     * @param taskList Task list into which saved tasks are loaded.
     * @return True if the data file exists, or false otherwise.
     * @throws IOException If the data file cannot be read.
     */
    public boolean loadInto(TaskList taskList) throws IOException {
        if (Files.notExists(this.filePath)) {
            return false;
        }

        List<String> lines = Files.readAllLines(this.filePath, StandardCharsets.UTF_8);
        List<Task> loadedTasks = new ArrayList<>();

        for (String line : lines) {
            if (!line.isBlank()) {
                loadedTasks.add(Parser.parseStoredTask(line));
            }
        }

        for (Task task : loadedTasks) {
            taskList.addTask(task);
        }
        return true;
    }

    /**
     * Saves all tasks from the specified task list to the data file.
     *
     * @param taskList Task list to save.
     * @throws IOException If the tasks cannot be saved.
     */
    public void saveFrom(TaskList taskList) throws IOException {
        List<String> lines = new ArrayList<>();

        for (Task task : taskList.getAllTasks()) {
            String line = task.toDataString();
            lines.add(line);
        }
        Files.write(this.filePath, lines, StandardCharsets.UTF_8);
    }
}
