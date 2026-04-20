package guiGradingRubric;

import java.util.List;
import grading.RubricCriterion;
import grading.RubricService;

/**
 * Model for the Grading Rubric GUI.
 * Delegates all computation to grading.RubricService – no arithmetic here.
 */
public class ModelGradingRubric {

    public ModelGradingRubric() {}

    public static List<RubricCriterion> getCriteria()              { return RubricService.getCriteria(); }
    public static int                   getMaxTotal()              { return RubricService.getMaxTotal(); }
    public static List<String>          getAllStudentUsernames()    { return RubricService.getAllStudentUsernames(); }
    public static int                   getPoints(String u, String c) { return RubricService.getPoints(u, c); }
    public static int                   getTotalGrade(String u)    { return RubricService.getTotalGrade(u); }
    public static boolean               hasGrade(String u)         { return RubricService.hasGrade(u); }
    public static String                buildStudentLabel(String u){ return RubricService.buildStudentLabel(u); }

    public static void setPoints(String username, String criterionName, int points) {
        RubricService.setPoints(username, criterionName, points);
    }

    public static void clearGrades(String username) {
        RubricService.clearGrades(username);
    }

    /** Computes live running total from spinner values – delegates to RubricService. */
    public static int computeRunningTotal(int[] pointsPerCriterion) {
        return RubricService.computeRunningTotal(pointsPerCriterion);
    }
}
