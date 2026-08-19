import java.util.Scanner;

public class Ari {
    public static void print(String s) {
        System.out.println(s);
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

        while (!flatInput.equals("bye")) {
            print(userInput);
            userInput = scanner.nextLine();
            flatInput = userInput.toLowerCase();
        }
        print(farewell);
        scanner.close(); // always clean up
    }
}
