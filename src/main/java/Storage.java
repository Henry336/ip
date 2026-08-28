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
    private static final Path FILE_PATH = Path.of("data", "ari.txt");

    /**
     * Creates the data directory and file when they do not already exist.
     */
    public static void start() {
        try {
            Files.createDirectories(FILE_PATH.getParent());

            if (Files.notExists(FILE_PATH)) {
                Files.createFile(FILE_PATH);
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
     * @param taskArray Task list into which saved tasks are loaded.
     */
    public static void loadInto(TaskArray taskArray) {
        try {
            if (Files.notExists(FILE_PATH)) {
                System.out.println("There are no saved tasks yet!");
                return;
            }

            List<String> lines = Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8);
            List<Task> loadedTasks = new ArrayList<>();

            for (String line : lines) {
                if (!line.isBlank()) {
                    loadedTasks.add(Parser.parseStoredTask(line));
                }
            }

            for (Task task : loadedTasks) {
                taskArray.addTask(task);
            }
            System.out.println("All tasks were loaded!");
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Sorry, I couldn't load your tasks: " + e.getMessage());
        }
    }

    /**
     * Saves all tasks from the specified task list to the data file.
     *
     * @param taskArray Task list to save.
     */
    public static void saveFrom(TaskArray taskArray) {
        List<String> lines = new ArrayList<>();

        for (Task task : taskArray.getAllTasks()) {
            String line = task.toDataString();
            lines.add(line);
        }
        try {
            Files.write(FILE_PATH, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Sorry, I couldn't save your tasks: " + e.getMessage());
            return;
        }

        System.out.println("I've saved your tasks.");
    }
}
