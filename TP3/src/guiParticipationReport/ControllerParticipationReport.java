package guiParticipationReport;

import entityClasses.Post;
import java.util.List;

/*******
 * <p> Title: ControllerParticipationReport Class </p>
 *
 * <p> Description: Handles all user interactions on the Student Participation Report
 * page.  All methods are static so the View can call them directly without holding
 * a controller reference. </p>
 *
 * <p> This controller supports User Story #7 – Student Participation Report Generation.
 * It coordinates between the View (what the user sees) and the Model (where data lives),
 * keeping the logic out of both. </p>
 *
 * <p> Tested by: ModelParticipationReportTest (JUnit 5) — GUI behavior covered by manual tests </p>
 *
 * @author Jack Ding
 * @version 1.00  2026-04-19  Initial implementation for TP3
 */
public class ControllerParticipationReport {

    /*****
     * <p> Default constructor — not used; all methods are static. </p>
     */
    public ControllerParticipationReport() {}

    /*****
     * <p> Navigates to the Account Update page. </p>
     */
    protected static void performUpdate() {
        guiUserUpdate.ViewUserUpdate.displayUserUpdate(
                ViewParticipationReport.theStage,
                ViewParticipationReport.theUser);
    }

    /*****
     * <p> Logs the current user out and returns to the login page. </p>
     */
    protected static void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewParticipationReport.theStage);
    }

    /*****
     * <p> Terminates the application. </p>
     */
    protected static void performQuit() {
        System.exit(0);
    }

    /*****
     * <p> Returns to the Staff Home page. </p>
     */
    protected static void performBack() {
        guiRole2.ViewRole2Home.displayRole2Home(
                ViewParticipationReport.theStage,
                ViewParticipationReport.theUser);
    }

    /*****
     * <p> Loads all students into the ComboBox and resets the report area. </p>
     *
     * <p> Called once when the page first appears and again whenever the user
     * Storing the post list in the View avoids re-querying the
     * database every time the user changes the selected student. </p>
     */
    protected static void loadStudents() {
        List<Post> allPosts = ModelParticipationReport.loadAllPosts();
        ViewParticipationReport.allPosts = allPosts;

        List<String> usernames = ModelParticipationReport.getStudentUsernames(allPosts);

        ViewParticipationReport.comboBox_Students.getItems().clear();
        ViewParticipationReport.contentData.clear();
        ViewParticipationReport.label_Stats.setText("Select a student above to view their report.");

        if (usernames.isEmpty()) {
            ViewParticipationReport.comboBox_Students.setPromptText("No students found");
        } else {
            ViewParticipationReport.comboBox_Students.getItems().addAll(usernames);
            ViewParticipationReport.comboBox_Students.setPromptText("Choose a student...");
        }
    }

    /*****
     * <p> Updates the stats label and content list for the currently selected student. </p>
     *
     * <p> Called each time the user picks a different name from the ComboBox.
     * If no student is selected (null), the method returns immediately so the page
     * stays in a valid state. </p>
     */
    protected static void selectStudent() {
        String selected = ViewParticipationReport.comboBox_Students.getValue();
        if (selected == null) return;

        List<Post> allPosts = ViewParticipationReport.allPosts;

        // Update the one-line participation summary at the top of the report area
        String stats = ModelParticipationReport.getSummaryStats(selected, allPosts);
        ViewParticipationReport.label_Stats.setText(stats);

        // Populate the detail list with the student's posts and replies
        ViewParticipationReport.contentData.clear();
        List<String> details = ModelParticipationReport.getPostDetails(selected, allPosts);

        if (details.isEmpty()) {
            ViewParticipationReport.contentData.add("No posts or replies found for: " + selected);
        } else {
            ViewParticipationReport.contentData.addAll(details);
        }
    }
}
