import java.util.Scanner;

public class Ari {
    public static int idx = 0;
    public static String[] list = new String[100];
    public static String GOODBYE = "bye";
    public static String LIST = "list";

    public static void print(String s) {
        System.out.println(s);
    }

    public static void printList(String[] list) {
        int init = 0;
        String result = "";

        while (init < idx) {
            int num = init + 1;
            result += String.format("%s. %s\n", num, list[init]);
            init += 1;
        }

        print(result);
    }

    public static void main(String[] args) {

        String banner =
                "   ----   \n"
                + "  / /\\ \\ \n"
                + " / /__\\ \\ \n"
                + "/ /    \\ \\ \n";

        String name = "Ari";
        Scanner scanner = new Scanner(System.in);

        String greeting = String.format("Heyyy, I'm %s!\n", name)
                + "What can I help you with?\n";

        String farewell = "Bye byeee. Take care!\n";

        print(banner);
        print(greeting);
        String userInput = scanner.nextLine();
        String flatInput = userInput.toLowerCase();

        while (!flatInput.equals(GOODBYE)) {
            if (!flatInput.equals(LIST)) {
                list[idx] = userInput;
                String addedText = String.format("added: %s", userInput);
                print(addedText);
                idx += 1;
            } else {
                printList(list);
            }
            userInput = scanner.nextLine();
            flatInput = userInput.toLowerCase();
        }

        print(farewell);
        scanner.close(); // always clean up
    }
}
