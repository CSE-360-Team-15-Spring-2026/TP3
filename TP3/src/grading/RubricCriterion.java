package grading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entityClasses.Post;
import gradingTools.gradingStatistics;

/*******
 * <p> Title: RubricService Class. </p>
 *
 * <p> Description: All rubric computation lives here. The GUI layer must never do
 * arithmetic – it only calls this class. </p>
 */
public class RubricService {

    /*-*******************************************************************************************

    Rubric definition

     */

    /** The list of rubric criteria used to evaluate student discussion posts. */
    private static final List<RubricCriterion> CRITERIA = new ArrayList<>();

    static {
        CRITERIA.add(new RubricCriterion(
            "Content Quality",
            "Post demonstrates accurate understanding of the topic",
            30));
        CRITERIA.add(new RubricCriterion(
            "Critical Thinking",
            "Post shows analysis, evaluation, or synthesis rather than mere summary",
            25));
        CRITERIA.add(new RubricCriterion(
            "Engagement",
            "Post replies to at least two unique classmates with substantive responses",
            20));
        CRITERIA.add(new RubricCriterion(
            "Clarity & Writing",
            "Post is clearly written, grammatically sound, and easy to follow",
            15));
        CRITERIA.add(new RubricCriterion(
            "Timeliness",
            "Post and replies were submitted before the deadline",
            10));
    }

    /**
     * <p> Returns the immutable list of rubric criteria. </p>
     *
     * @return unmodifiable list of all rubric criteria
     */
    public static List<RubricCriterion> getCriteria() {
        return Collections.unmodifiableList(CRITERIA);
    }

    /**
     * <p> Returns the maximum achievable total across all criteria. </p>
     *
     * @return the sum of max points for every criterion
     */
    public static int getMaxTotal() {
        int sum = 0;
        for (RubricCriterion c : CRITERIA) sum += c.getMaxPoints();
        return sum;
    }


    /*-*******************************************************************************************

    Per-student score storage

     */

    /** Maps each username to a map of criterion names and their awarded points. */
    // username → (criterionName → awarded points)
    private static final Map<String, Map<String, Integer>> scores = new HashMap<>();

    /**
     * <p> Records points for one criterion for one student.
     * Points are clamped to [0, criterion.maxPoints]. </p>
     *
     * @param username      the student's username
     * @param criterionName the name of the criterion being scored
     * @param points        the raw point value to record
     */
    public static void setPoints(String username, String criterionName, int points) {
        int max = 0;
        for (RubricCriterion c : CRITERIA) {
            if (c.getName().equals(criterionName)) { max = c.getMaxPoints(); break; }
        }
        int clamped = Math.max(0, Math.min(points, max));
        scores.computeIfAbsent(username, k -> new HashMap<>()).put(criterionName, clamped);
    }

    /**
     * <p> Returns the awarded points for one criterion for one student, or 0 if not yet set. </p>
     *
     * @param username      the student's username
     * @param criterionName the name of the criterion to look up
     * @return the awarded points, or 0 if not yet recorded
     */
    public static int getPoints(String username, String criterionName) {
        Map<String, Integer> row = scores.get(username);
        return (row == null) ? 0 : row.getOrDefault(criterionName, 0);
    }

    /**
     * <p> Returns the sum of all awarded criterion points for a student. </p>
     *
     * @param username the student's username
     * @return the total grade for that student
     */
    public static int getTotalGrade(String username) {
        Map<String, Integer> row = scores.get(username);
        if (row == null) return 0;
        int sum = 0;
        for (int v : row.values()) sum += v;
        return sum;
    }

    /**
     * <p> Returns true if at least one criterion has been saved for this student. </p>
     *
     * @param username the student's username
     * @return true if any criterion score exists, false otherwise
     */
    public static boolean hasGrade(String username) {
        Map<String, Integer> row = scores.get(username);
        return row != null && !row.isEmpty();
    }

    /**
     * <p> Clears all stored criterion scores for a student. </p>
     *
     * @param username the student's username
     */
    public static void clearGrades(String username) {
        scores.remove(username);
    }


    /*-*******************************************************************************************

    Student list helpers

     */

    /**
     * <p> Returns every student username that has posted in the system. </p>
     *
     * @return list of all student usernames
     */
    public static List<String> getAllStudentUsernames() {
        List<Post> allPosts = applicationMain.FoundationsMain.database.getAllPosts();
        return gradingStatistics.getAllStudentUsernames(allPosts);
    }

    /**
     * <p> Builds the display string shown in the student ListView.
     * Shows awarded/max when graded, otherwise "[not graded]". </p>
     *
     * @param username the student's username
     * @return a formatted label string for display in the UI
     */
    public static String buildStudentLabel(String username) {
        if (hasGrade(username)) {
            return username + "  [" + getTotalGrade(username) + "/" + getMaxTotal() + " pts]";
        }
        return username + "  [not graded]";
    }

    /**
     * <p> Computes the running total from an array of raw point values,
     * one per criterion in CRITERIA order. Used for live preview in the UI. </p>
     *
     * @param pointsPerCriterion array of point values in criteria order
     * @return the sum of all values in the array
     */
    public static int computeRunningTotal(int[] pointsPerCriterion) {
        int sum = 0;
        for (int p : pointsPerCriterion) sum += p;
        return sum;
    }
}
}
