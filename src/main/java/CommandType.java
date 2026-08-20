public enum CommandType {
    MARK("Good job! I've marked this task as done:"),
    UNMARK("Gotcha, I've unmarked this task:"),
    EXIT("Bye Bye. See you again!"),
    BYE("Bye Bye. See you again!"),
    LIST("Here are the tasks on your list:"),
    ADD("Added: "),
    TODO("Gotcha. I've added this task for you:"),
    UNKNOWN("Error"),
    DEADLINE("Gotcha. I've added this task for you:"),
    EVENT("Gotcha. I've added this task for you:"),
    DELETE("Done! I've removed the task for you:");

    private final String description;

    /**
     * The following is an implicitly private constructor
     * for each command type (MARK, UNMARK, EXIT, etc.)
     *
     * Added so that I can set each one's text message easily
     * @param description
     */
    CommandType(String description) {
        this.description = description;
    }

    /**
     * Regular method for each command type
     * @return command's description of type String
     */
    public String getDescription() {
        return this.description;
    }

    public static CommandType of(String rawInput) {
        if (rawInput == null || rawInput.isBlank()) {
            // don't want to try and accept a null or blank space yet
            return UNKNOWN;
        }

        String cmd = rawInput.strip().split("\\s+")[0];
        // .strip() takes away all the trailing whitespace like python does
        // \\s means any 'whitespace' character that may be in the middle
        // + means one or more times

        try {
            return CommandType.valueOf(cmd.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN; // like 'read book', 'do homework', 'study', etc.
        }
    }
}
