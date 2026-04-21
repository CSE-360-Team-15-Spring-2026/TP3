package guiGradingRubric;

import java.util.List;
import grading.RubricCriterion;
import grading.RubricService;

/*******
 * <p> Title: ModelGradingRubric Class. </p>
 *
 * <p> Description: Model for the Grading Rubric GUI. Delegates all computation to
 * grading.RubricService — no arithmetic here. </p>
 */
public class ModelGradingRubric {

    /*-*******************************************************************************************

    Constructor

     */

    /**
     * Default constructor is not used (all methods are static).
     */
    public ModelGradingRubric() {}


    /*-*******************************************************************************************

    Delegation methods

     */

    /**
     * <p> Returns the list of rubric criteria from RubricService. </p>
     *
     * @return the list of rubric criteria
     */
    public static List<RubricCriterion> getCriteria()              { return RubricService.getCriteria(); }

    /**
     * <p> Returns the maximum achievable total from RubricService. </p>
     *
     * @return the maximum total points
     */
    public static int                   getMaxTotal()              { return RubricService.getMaxTotal(); }

    /**
     * <p> Returns all student usernames that have posted in the system. </p>
     *
     * @return list of student usernames
     */
    public static List<String>          getAllStudentUsernames()    { return RubricService.getAllStudentUsernames(); }

    /**
     * <p> Returns the awarded points for one criterion for one student. </p>
     *
     * @param u the student's username
     * @param c the criterion name
     * @return the awarded points, or 0 if not yet recorded
     */
    public static int                   getPoints(String u, String c) { return RubricService.getPoints(u, c); }

    /**
     * <p> Returns the total grade for a student across all criteria. </p>
     *
     * @param u the student's username
     * @return the total awarded points
     */
    public static int                   getTotalGrade(String u)    { return RubricService.getTotalGrade(u); }

    /**
     * <p> Returns true if at least one criterion score has been saved for this student. </p>
     *
     * @param u the student's username
     * @return true if any score exists, false otherwise
     */
    public static boolean               hasGrade(String u)         { return RubricService.hasGrade(u); }

    /**
     * <p> Builds the display string shown in the student ListView. </p>
     *
     * @param u the student's username
     * @return a formatted label string for display in the UI
     */
    public static String                buildStudentLabel(String u){ return RubricService.buildStudentLabel(u); }

    /**
     * <p> Records points for one criterion for one student. </p>
     *
     * @param username      the student's username
     * @param criterionName the name of the criterion being scored
     * @param points        the point value to record
     */
    public static void setPoints(String username, String criterionName, int points) {
        RubricService.setPoints(username, criterionName, points);
    }

    /**
     * <p> Clears all stored criterion scores for a student. </p>
     *
     * @param username the student's username
     */
    public static void clearGrades(String username) {
        RubricService.clearGrades(username);
    }

    /**
     * <p> Computes the live running total from spinner values by delegating to
     * RubricService. </p>
     *
     * @param pointsPerCriterion array of point values in criteria order
     * @return the sum of all values in the array
     */
    public static int computeRunningTotal(int[] pointsPerCriterion) {
        return RubricService.computeRunningTotal(pointsPerCriterion);
    }
}
