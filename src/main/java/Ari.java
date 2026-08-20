import java.util.Scanner;

public class Ari {
    public static String name = "Ari";
    public static int idx = 0;
    public static TaskList tasks = new TaskList(100);

    public static String banner =
            "   ----   \n"
                    + "  / /\\ \\ \n"
                    + " / /__\\ \\ \n"
                    + "/ /    \\ \\ \n";

    public static void print(String s) {
        System.out.println(s);
    }

    public static int getIdx(String s) {
        String result = s.strip().split("\\s+")[1];
        return Integer.parseInt(result);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        print(banner);
        print("Hey, I'm Ari!\n" + "What can I help you with?\n");

        while (true) {
            String input = scanner.nextLine();
            CommandType command = CommandType.of(input);

            if (command.equals(CommandType.EXIT)) {
                print(command.getDescription());
                break;
            }

            switch (command) {
                case LIST:
                    print(command.getDescription());
                    print(tasks.toString());
                    break;

                case MARK:
                case UNMARK:
                    int idx = getIdx(input);
                    String changedTask = tasks.changeTaskState(idx);
                    print(command.getDescription());
                    print(changedTask);
                    break;

                case ADD:
                    String addedTask = tasks.addTask(input);
                    print(command.getDescription() + addedTask);
                    break;
            }
        }

        scanner.close(); // always clean up
    }
}
