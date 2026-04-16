package api;

import api.config.FridgeBeansConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * 按绿色三角跑起来 = 开 HTTP 服务给 Flutter / Run this class (or {@code bootRun}) to start the HTTP API for Flutter.
 * <p>命令行玩游戏还是走 {@link ui.Main} / Text game still uses {@link ui.Main}.</p>
 * @see team.TeamModuleBacklog 分工清单 / team task list
 */
@SpringBootApplication
@Import(FridgeBeansConfig.class)
public class SmartFridgeApiApplication {

    public static void main(String[] args) {
        // 提醒组长：对照 README 里的图，看「控制台有的功能」是不是「网页也都有」；缺的就补测试或补接口。
        // Lead: match README features to HTTP endpoints; add tests when you add routes.
        // INSERT YOUR CODE HERE
        SpringApplication.run(SmartFridgeApiApplication.class, args);
    }
}
