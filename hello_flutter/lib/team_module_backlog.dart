/// 组员分块 backlog（仅说明文档，无运行逻辑；勿当业务库 import）。
///
/// --- 块 F1 — 购物清单 (`screens/grocery_list_screen.dart`) — 已接 API ---
/// - 可选：后端若增加 `DELETE /api/grocery/items/{id}` 再补「删除行」；单价输入 UI。
///
/// --- 块 F2 — 设置 (`screens/settings_screen.dart`) — 已接 GET/PUT preference ---
/// - 可选：与 `PreferencesScreen` 视觉统一；通知/账号仍为占位。
///
/// --- 块 F3 — 菜谱详情 (`screens/recipe_detail_screen.dart`) — 已用列表 JSON 的 description + 必选行/在架 ---
/// - 若日后有 `GET /api/recipes/{id}` 可再切换数据源。
///
/// --- 块 F4 — 库存添加 (`screens/inventory_screen.dart`) — 已接 catalog suggestions ---
/// - 可选：删除/改库存数量（需后端 inventory API 扩展）。
///
/// --- 块 F5 — 欢迎与健壮性 (`screens/welcome_screen.dart` / `main.dart`) ---
/// - 启动前 `GET /api/inventory` 或专用 health 探测；失败时明确引导用户 `bootRun`。
/// - 全局错误样式、重试、超时（`http` Client 配置）。
///
/// --- 块 F6 — 测试与工程化 ---
/// - `FridgeApiService` 的 mock 测试；golden / widget 测试关键屏在 mock 服务器下的表现。
/// - 环境区分：dev/staging `dart-define` 文档化。
///
/// --- 块 X — 与 Java 组对齐 ---
/// - DTO 字段名、日期格式、枚举字符串与 OpenAPI 或 README 表格同步，避免双端漂移。
library;
