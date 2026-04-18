package api.dto;

/**
 * PUT /api/preference 的 JSON / JSON for saving health goal.
 * <p>合法字符串 / Valid strings: MUSCLE_BUILDING, FAT_LOSS, BLOOD_SUGAR_CARE.</p>
 */
public class PreferenceRequest {
    // 以后 PRD 如果要多问几个问题（比如过敏），再一起改 Preference 和这个类 / Extend when PRD extends Preference.
    // INSERT YOUR CODE HERE

    private String healthGoal;

    public String getHealthGoal() {
        return healthGoal;
    }

    public void setHealthGoal(String healthGoal) {
        this.healthGoal = healthGoal;
    }
}
