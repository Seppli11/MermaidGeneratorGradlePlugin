package ch.zhaw.catan;
/**
 * Provides the main entrypoint of the application.
 */
public class App {
    /**
     * Runs the main entrypoint of the application.
     *
     * @param args
     * 		The arguments provided by the user.
     */
    public static void main(java.lang.String[] args) {
        new ch.zhaw.catan.Controller().run();
    }
}