package ui;

/**
 * Legacy Java console/demo entry used by Gradle {@code run}.
 * <p>
 * The official product frontend is {@code hello_flutter}; this class remains only for backward
 * compatibility with the older console flow.
 */
public class Main {

    /**
     * Program entry; delegates composition root startup to {@link SmartFridgeApp}.
     *
     * @param args unused in current demo
     */
    public static void main(String[] args) {
        SmartFridgeApp.main(args);
    }
}
