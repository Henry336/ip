public class Ari {
    public static void main(String[] args) {
        String banner =
                "   ----   \n"
                + "  / /\\ \\ \n"
                + " / /__\\ \\ \n"
                + "/ /    \\ \\ \n";

        String name = "Ari";
        String greeting = String.format("Hello! I'm %s.\n", name)
                + "What can I do for you?\n"
                + "Bye. Hope to see you again soon!\n";

        System.out.println(banner);
        System.out.println(greeting);
    }
}
