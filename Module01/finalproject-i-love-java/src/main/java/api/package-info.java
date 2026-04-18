/**
 * 这个 {@code api} 包 = 给手机/网页用的 HTTP 接口层 / This package is the HTTP layer for Flutter etc.
 * <p>
 * 它里面<strong>已经有真的代码</strong>（不是空壳）：会调用你们写好的 controller + service。
 * It already contains <strong>real code</strong> (not empty shells): it calls your {@code controller} + {@code service}.
 * </p>
 * <p>
 * 它和「只有组员控制台」不一样的地方 / What is extra vs console-only teammates' work:
 * Spring Boot 启动、地址（URL）、跨域 CORS、还有把 JSON 转成 Java 对象的小类。
 * Spring Boot startup, URLs, CORS, and small JSON DTO classes.
 * </p>
 * <p>
 * 哪里还没对齐图纸/PRD / What may still be missing vs README + PRD:
 * 看每个 {@code *ApiController} 上面的说明 + 文件里的 {@code // INSERT YOUR CODE HERE}。
 * Read each {@code *ApiController} header and the {@code // INSERT YOUR CODE HERE} lines.
 * </p>
 * </p>
 */
package api;
