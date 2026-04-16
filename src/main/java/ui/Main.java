package ui;

/**
 * Gradle {@code application} plugin entry class; forwards to {@link SmartFridgeApp#main(String[])}.
 * <p>
 * Gradle {@code application} 插件配置的入口类；转发到 {@link SmartFridgeApp#main(String[])}。
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
