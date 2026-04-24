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
     * <p>
     * 程序入口；将组合根启动委托给 {@link SmartFridgeApp}。
     *
     * @param args unused in current demo / 当前演示未使用
     */
    public static void main(String[] args) {
        SmartFridgeApp.main(args);
    }
}
