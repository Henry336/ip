import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static Path filePath = Path.of("data", "ari.txt");

    /**
     * Creates the directory and file from
     * the specified file path if they
     * don't already exist
     */
    public static void start() {
        try {
            Files.createDirectories(filePath.getParent());

            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            System.out.println(
                    "Sorry, I couldn't initialize the storage file: "
                    + e.getMessage()
            );
        }
    }

    /**
     * Loads the saved data from the specified file path
     *
     * @param taskArray
     */
    public static void loadInto(TaskArray taskArray) {
        try {
            if (Files.notExists(filePath)) {
                System.out.println("There are no saved tasks yet!");
            }

            List<String> lines = Files
                    .readAllLines(
                            filePath,
                            StandardCharsets.UTF_8
                    );

            for (String line : lines) {
                String[] parts = line.split("\\s*\\|\\s*", 3);

                String type = parts[0];
                String status = parts[1];
                String taskData = parts[2];

                Task task;
                switch (type) {
                    case "D":
                        task = new DeadlineTask(taskData);
                        break;
                    case "E":
                        task = new EventTask(taskData);
                        break;
                    default:
                        task = new TodoTask(taskData);
                        break;
                }

                if (status.equals("1")) {
                    task.markTask();
                } else if (!status.equals("0")) {
                    throw new IllegalArgumentException("Invalid task status: " + status);
                }

                taskArray.addTask(task);
            }
        } catch (IOException e) {
            System.out.println("Sorry, I couldn't load your tasks: " + e.getMessage());
        } catch (EmptyArgumentException e) {
            System.out.println("Sorry, there's something wrong with your task: " + e.getMessage());
        }

        System.out.println("All tasks were loaded!");
    }

    /**
     * Saves the new task array to the specified file path
     *
     * @param taskArray
     */
    public static void saveFrom(TaskArray taskArray) {
        ArrayList<String> lines = new ArrayList<>();

        for (Task task : taskArray.getAllTasks()) {
            String line = task.toDataString();
            lines.add(line);
        }
        try {
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Sorry, I couldn't save your tasks: " + e.getMessage());
            return;
        }

        System.out.println("I've saved your tasks.");
    }
}
