package ari.exception;

public class EmptyArgumentException extends Exception {
    public EmptyArgumentException(String taskType) {
        super(String.format("Oh no! You can't have an empty description for %ss", taskType));
    }
}
