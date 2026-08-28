import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static Path filePath = Path.of("data", "ari.txt");

    /**
     * Loads the saved data from the specified file path
     */
    public static void load() {
        try {
            if (Files.notExists(filePath)) {
                System.out.println("There is no saved data yet!");
                return;
            }

            List<String> lines = Files
                    .readAllLines(
                            filePath,
                            StandardCharsets.UTF_8
                    );
        } catch (IOException e) {
            System.out.println("Could not load the data: " + e.getMessage());
        }
    }

    /**
     * Saves the new task array to the specified file path
     *
     * @param taskArray
     * @throws IOException
     */
    public static void save(TaskArray taskArray) throws IOException {
        ArrayList<String> lines = new ArrayList<>();

        for (Task task : taskArray.getAllTasks()) {
            String line = task.toDataString();
            lines.add(line);
        }

        Files.write(filePath, lines, StandardCharsets.UTF_8);
        System.out.println("Tasks successfully saved!");
    }
}
