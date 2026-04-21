package grading;

/*******
 * <p> Title: GradingRecord Class. </p>
 *
 * <p> Description: The GradingRecord class stores review and grading information for a single
 * discussion item. This prototype supports the instructional-team workflow for marking content
 * as reviewed and assigning grade points.
 *
 * Each record tracks whether the content has been reviewed, what grade (if any) has been
 * assigned, and which grader performed the action. </p>
 */
public class GradingRecord {

    private int contentId;
    private boolean reviewed;
    private Integer grade;
    private String graderUsername;

     /**
     * Default constructor is not used.
     */
    public GradingRecord(int contentId) {
        this.contentId = contentId;
        this.reviewed = false;
        this.grade = null;
        this.graderUsername = null;
    }

    /**
     * <p> Returns the content ID associated with this grading record. </p>
     *
     * @return the integer content ID
     */
    public int getContentId() {
        return contentId;
    }

    /**
     * <p> Returns whether the content item has been marked as reviewed. </p>
     *
     * @return true if reviewed, false otherwise
     */
    public boolean isReviewed() {
        return reviewed;
    }

     /**
     * <p> Sets the reviewed state of this grading record. </p>
     *
     * @param reviewed the new reviewed state
     */
    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    /**
     * <p> Returns the grade assigned to the content item, or null if none has been set. </p>
     *
     * @return the assigned grade, or null
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * <p> Sets the grade for the content item associated with this record. </p>
     *
     * @param grade the grade to store, or null to clear it
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    /**
     * <p> Returns the username of the grader who reviewed or graded this content item. </p>
     *
     * @return the grader's username, or null if not yet assigned
     */
    public String getGraderUsername() {
        return graderUsername;
    }

    /**
     * <p> Sets the username of the grader associated with this record. </p>
     *
     * @param graderUsername the grader's username
     */
    public void setGraderUsername(String graderUsername) {
        this.graderUsername = graderUsername;
    }
}
