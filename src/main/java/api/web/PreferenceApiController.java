package api.web;

import api.dto.PreferenceRequest;
import controller.PreferenceController;
import model.HealthGoal;
import model.Preference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康目标（增肌/减脂/控糖）/ Health goal for scoring recipes.
 * <p>Now 现在: GET 读当前选择；PUT 用字符串保存（MUSCLE_BUILDING 等）。</p>
 */
@RestController
@RequestMapping("/api/preference")
public class PreferenceApiController {

    private final PreferenceController preferenceController;

    public PreferenceApiController(PreferenceController preferenceController) {
        this.preferenceController = preferenceController;
    }

    // 可选：如果 PRD 要「一键恢复默认目标」——服务里 clearPreference 已有，网页还没接口。
    // Optional: HTTP "reset to default" → call clearPreference. 没说要加字段就别乱加 / Don't add fields PRD does not ask for.
    // INSERT YOUR CODE HERE

    @GetMapping
    public Preference get() {
        return preferenceController.getPreference();
    }

    @PutMapping
    public ResponseEntity<Preference> save(@RequestBody PreferenceRequest body) {
        if (body == null || body.getHealthGoal() == null || body.getHealthGoal().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            HealthGoal goal = HealthGoal.valueOf(body.getHealthGoal().trim().toUpperCase());
            return ResponseEntity.ok(preferenceController.savePreference(goal));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
