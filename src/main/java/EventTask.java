/**
 * Represents a task that takes place between a start and end time.
 */
public class EventTask extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task from an event command.
     *
     * @param command Full event command entered by the user.
     * @throws EmptyArgumentException If the description, start, or end is empty.
     */
    public EventTask(String command) throws EmptyArgumentException {
        this(parseCommand(command));
    }

    private EventTask(String[] commandParts) {
        this(commandParts[0], commandParts[1], commandParts[2]);
    }

    /**
     * Creates an event task from separately parsed storage fields.
     *
     * @param description Event description prefixed by its command word.
     * @param from Start time of the event.
     * @param to End time of the event.
     */
    public EventTask(String description, String from, String to) {
        super(description, "E");
        this.from = from;
        this.to = to;
    }

    private static String[] parseCommand(String command) throws EmptyArgumentException {
        String[] commandParts = command.strip().split("\\s+/from\\s+", 2);
        if (commandParts.length < 2) {
            throw new EmptyArgumentException("event");
        }

        String[] descriptionParts = commandParts[0].split("\\s+", 2);
        String[] timeParts = commandParts[1].split("\\s+/to\\s+", 2);
        boolean hasDescription = descriptionParts.length == 2 && !descriptionParts[1].isBlank();
        boolean hasTimes = timeParts.length == 2 && !timeParts[0].isBlank() && !timeParts[1].isBlank();
        if (!hasDescription || !hasTimes) {
            throw new EmptyArgumentException("event");
        }

        return new String[] {
                commandParts[0].strip(),
                timeParts[0].strip(),
                timeParts[1].strip()
        };
    }

    public String[] getEventTime() {
        return new String[]{this.from, this.to};
    }

    @Override
    public String toDataString() {
        String status = isDone
                ? "1"
                : "0";

        return String.format(
                "%s | %s | %s (from: %s | to: %s)",
                this.type,
                status,
                this.description,
                this.from,
                this.to
        );
    }

    @Override
    public String toString() {
        String box;

        if (this.isDone) {
            box = "[X]";
        } else {
            box = "[ ]";
        }

        return String.format(
                "[%s]%s %s (from: %s to: %s)",
                this.type, box, this.description, this.from, this.to);
    }
}
