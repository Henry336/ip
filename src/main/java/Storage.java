import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
     */
    public void start() {
        try {
            Files.createDirectories(this.filePath.getParent());

            if (Files.notExists(this.filePath)) {
                Files.createFile(this.filePath);
            }
        } catch (IOException e) {
            System.out.println(
                    "Sorry, I couldn't initialize the storage file: "
                    + e.getMessage()
            );
        }
    }

    /**
     * Loads all valid saved tasks into the specified task list.
     *
     * @param taskList Task list into which saved tasks are loaded.
     */
    public void loadInto(TaskList taskList) {
        try {
            if (Files.notExists(this.filePath)) {
                System.out.println("There are no saved tasks yet!");
                return;
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
            System.out.println("All tasks were loaded!");
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Sorry, I couldn't load your tasks: " + e.getMessage());
        }
    }

    /**
     * Saves all tasks from the specified task list to the data file.
     *
     * @param taskList Task list to save.
     */
    public void saveFrom(TaskList taskList) {
        List<String> lines = new ArrayList<>();

        for (Task task : taskList.getAllTasks()) {
            String line = task.toDataString();
            lines.add(line);
        }
        try {
            Files.write(this.filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Sorry, I couldn't save your tasks: " + e.getMessage());
            return;
        }

        System.out.println("I've saved your tasks.");
    }
}
