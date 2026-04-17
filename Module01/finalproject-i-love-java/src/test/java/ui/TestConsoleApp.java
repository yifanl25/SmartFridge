package ui;

import controller.GroceryController;
import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;
import model.FoodCatalogEntry;
import model.FoodCategory;
import service.FoodCatalog;
import service.GroceryService;
import service.InventoryService;
import service.PreferenceService;
import service.RecommendationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * TDD：文本控制台命令行入口（对应 {@link ConsoleApp}，可选演示路径）。
 */
public class TestConsoleApp {
    private ConsoleApp consoleApp;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        InventoryController inventoryController =
                new InventoryController(new InventoryService(new FoodCatalog(List.of(new FoodCatalogEntry("Milk", 7, cat)))));
        PreferenceController preferenceController = new PreferenceController(new PreferenceService());
        RecommendationController recommendationController =
                new RecommendationController(new RecommendationService(List.of(), new FoodCatalog(List.of())));
        GroceryController groceryController = new GroceryController(new GroceryService(List.of()));
        consoleApp = new ConsoleApp(
                inventoryController,
                preferenceController,
                recommendationController,
                groceryController,
                new FoodCatalog(List.of(new FoodCatalogEntry("Milk", 7, cat))));
    }

    /**
     * 测试功能：{@link ConsoleApp#start()} 非阻塞启动（仅打印提示）。
     * 验证点：不抛异常。
     * <p>
     * 对应源码 / Maps to: {@link ConsoleApp#start()}
     */
    @Test
    void testStartInitializesConsoleFlow() {
        assertDoesNotThrow(() -> consoleApp.start());
    }

    /**
     * 测试功能：解析 {@code add <name>}，委托库存添加。
     * 验证点：不抛异常。
     * <p>
     * 对应源码 / Maps to: {@link ConsoleApp#processCommand(String)} → {@link controller.InventoryController#addItem(String)}
     */
    @Test
    void testProcessCommandRoutesInventoryCommand() {
        assertDoesNotThrow(() -> consoleApp.processCommand("add Milk"));
    }

    /**
     * 测试功能：解析 {@code goal <goal>}，保存健康目标偏好。
     * 验证点：不抛异常。
     * <p>
     * 对应源码 / Maps to: {@link ConsoleApp#processCommand(String)} → {@link controller.PreferenceController#savePreference(model.HealthGoal)}
     */
    @Test
    void testProcessCommandRoutesPreferenceCommand() {
        assertDoesNotThrow(() -> consoleApp.processCommand("goal fat_loss"));
    }

    /**
     * 测试功能：未知命令（如 {@code noop}）的容错。
     * 验证点：不抛异常。
     * <p>
     * 对应源码 / Maps to: {@link ConsoleApp#processCommand(String)}（noop 分支无下游调用）
     */
    @Test
    void testProcessCommandRoutesRecommendationCommand() {
        assertDoesNotThrow(() -> consoleApp.processCommand("noop"));
    }

    /**
     * 测试功能：解析 {@code checkout}，触发购物结账与会话重置钩子。
     * 验证点：不抛异常。
     * <p>
     * 对应源码 / Maps to: {@link ConsoleApp#processCommand(String)} → {@link controller.GroceryController#checkout()}
     */
    @Test
    void testProcessCommandRoutesGroceryCommand() {
        assertDoesNotThrow(() -> consoleApp.processCommand("checkout"));
    }

    /**
     * 测试功能：空输入 {@code null} 的健壮性。
     * 验证点：不抛异常。
     * <p>
     * 对应源码 / Maps to: {@link ConsoleApp#processCommand(String)}（null 早退）
     */
    @Test
    void testProcessCommandHandlesInvalidInput() {
        assertDoesNotThrow(() -> consoleApp.processCommand(null));
    }
}
