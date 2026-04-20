package guiGradingRubric;

import java.util.List;
import grading.RubricCriterion;
import javafx.scene.control.Spinner;

public class ControllerGradingRubric {

    public ControllerGradingRubric() {}

    /** Populates the student ListView when the page opens. */
    public static void initialize() {
        List<String> usernames = ModelGradingRubric.getAllStudentUsernames();
        ViewGradingRubric.studentData.clear();
        ViewGradingRubric.tempUsernames = usernames;

        if (usernames.isEmpty()) {
            ViewGradingRubric.studentData.add("No students found");
            return;
        }
        for (String u : usernames) {
            ViewGradingRubric.studentData.add(ModelGradingRubric.buildStudentLabel(u));
        }
    }

    /** Called when a row in the student list is clicked. */
    public static void selectStudent() {
        int idx = ViewGradingRubric.list_Students.getSelectionModel().getSelectedIndex();
        if (idx < 0 || ViewGradingRubric.tempUsernames == null
                || idx >= ViewGradingRubric.tempUsernames.size()) return;

        String username = ViewGradingRubric.tempUsernames.get(idx);
        ViewGradingRubric.currentStudent = username;
        ViewGradingRubric.label_SelectedStudent.setText("Grading: " + username);

        // Load saved scores into spinners
        List<RubricCriterion> criteria = ModelGradingRubric.getCriteria();
        for (int i = 0; i < criteria.size(); i++) {
            int pts = ModelGradingRubric.getPoints(username, criteria.get(i).getName());
            ViewGradingRubric.criterionSpinners.get(i).getValueFactory().setValue(pts);
        }
        refreshTotal();
    }

    /** Persists the current spinner values for the selected student. */
    public static void saveGrades() {
        String username = ViewGradingRubric.currentStudent;
        if (username == null || username.isBlank()) {
            ViewGradingRubric.showAlert("No Student Selected",
                "Please select a student from the list before saving.");
            return;
        }

        List<RubricCriterion> criteria = ModelGradingRubric.getCriteria();
        for (int i = 0; i < criteria.size(); i++) {
            int pts = ViewGradingRubric.criterionSpinners.get(i).getValue();
            ModelGradingRubric.setPoints(username, criteria.get(i).getName(), pts);
        }

        initialize(); // refresh student list labels
        ViewGradingRubric.showAlert("Saved",
            "Grades saved for " + username + ".\n"
            + "Total: " + ModelGradingRubric.getTotalGrade(username)
            + " / " + ModelGradingRubric.getMaxTotal() + " pts");
    }

    /** Zeros out all spinners for the selected student (does not save). */
    public static void clearGrades() {
        String username = ViewGradingRubric.currentStudent;
        if (username == null || username.isBlank()) return;

        for (Spinner<Integer> sp : ViewGradingRubric.criterionSpinners) {
            sp.getValueFactory().setValue(0);
        }
        ModelGradingRubric.clearGrades(username);
        initialize();
        refreshTotal();
    }

    /**
     * Reads current spinner values, delegates the sum to the model,
     * and pushes the result into the total label.
     * Called on every spinner change.
     */
    public static void refreshTotal() {
        List<RubricCriterion> criteria = ModelGradingRubric.getCriteria();
        int[] pts = new int[criteria.size()];
        for (int i = 0; i < criteria.size(); i++) {
            pts[i] = ViewGradingRubric.criterionSpinners.get(i).getValue();
        }
        int running = ModelGradingRubric.computeRunningTotal(pts);
        ViewGradingRubric.label_Total.setText(
            "Total: " + running + " / " + ModelGradingRubric.getMaxTotal());
    }

    public static void performBack() {
        guiRole2.ViewRole2Home.displayRole2Home(
            ViewGradingRubric.theStage, ViewGradingRubric.theUser);
    }

    public static void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewGradingRubric.theStage);
    }
}