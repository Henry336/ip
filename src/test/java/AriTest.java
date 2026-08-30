package ari;

import java.io.IOException;

import ari.exception.EmptyArgumentException;
import ari.exception.TaskNotFoundException;
import ari.storage.Storage;
import ari.task.Task;
import ari.task.TaskList;
import ari.ui.Ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AriTest {
    @Test
    public void dummyTest() {
        assertEquals(2, 2);
    }

    @Test
    public void dummyTest2(){
        assertEquals(4, 3);
    }
}
