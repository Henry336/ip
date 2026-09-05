package ari;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class AriTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void getResponse_addThenList_returnsUpdatedTaskList() {
        Ari ari = new Ari(this.temporaryDirectory.resolve("ari.txt").toString());
        ari.start();

        String addResponse = ari.getResponse("todo read JavaFX tutorial");
        String listResponse = ari.getResponse("list");

        assertEquals(
                "Gotcha. I've added this task for you:\n"
                        + " [T][ ] read JavaFX tutorial\n"
                        + "You currently have 1 task in the list! Better get working...",
                addResponse
        );
        assertEquals(
                "Here are the tasks on your list:\n1. [T][ ] read JavaFX tutorial",
                listResponse
        );
    }

    @Test
    public void getResponse_invalidTaskId_returnsHelpfulError() {
        Ari ari = new Ari(this.temporaryDirectory.resolve("ari.txt").toString());
        ari.start();

        String response = ari.getResponse("mark one");

        assertEquals("Oops! You can only enter integer IDs. Try again!", response);
    }
}
