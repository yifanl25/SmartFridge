package ui;

import controller.PreferenceController;
import model.HealthGoal;

public class PreferencePage {
    // Controller gateway for preference setup actions.
    private final PreferenceController preferenceController;

    // Wire preference page to controller.
    public PreferencePage(PreferenceController preferenceController) {
        this.preferenceController = preferenceController;
    }

    // Render preference setup view where user picks one health goal.
    public void render() {
        System.out.println("Preference Page");
    }

    // Submit selected goal and store in session preference.
    public void submitGoal(HealthGoal goal) {
        preferenceController.savePreference(goal);
    }
}
