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
        print("Hola, I'm Ari!\n" + "Need any help?\n");

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
                    print(String.format(
                            "____________________________________________________________\n" +
                                    "%s\n %s\n" +
                            "____________________________________________________________\n",
                            command.getDescription(),
                            changedTask)
                    );
                    break;

                case TODO:
                    String addedTaskT = tasks.addTask(input, "T");
                    print(String.format(
                            "____________________________________________________________\n" +
                                    "%s\n %s\n%s\n" +
                            "____________________________________________________________\n",
                            command.getDescription(),
                            addedTaskT.toString(),
                            tasks.getLengthText()
                    ));
                    break;

                case DEADLINE:
                    String addedTaskD = tasks.addTask(input, "D");
                    print(String.format(
                            "____________________________________________________________\n" +
                                    "%s\n %s\n%s\n" +
                            "____________________________________________________________\n",
                            command.getDescription(),
                            addedTaskD.toString(),
                            tasks.getLengthText()
                    ));
                    break;

                case EVENT:
                    String addedTaskE = tasks.addTask(input, "E");
                    print(String.format(
                            "____________________________________________________________\n" +
                                    "%s\n %s\n%s\n" +
                            "____________________________________________________________\n",
                            command.getDescription(),
                            addedTaskE.toString(),
                            tasks.getLengthText()
                    ));
                    break;

                case UNKNOWN:
                    print("Sorry, I didn't get that... Could you say something else? ^.^\"");
                    break;
            }
        }

        scanner.close(); // always clean up
    }
}
