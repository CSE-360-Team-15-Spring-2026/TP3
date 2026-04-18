package grading;

/**
 * Stores review and grading information for one discussion item.
 * This prototype supports the instructional-team workflow for
 * marking content as reviewed and assigning grade points.
 *
 */
public class GradingRecord {

    private int contentId;
    private boolean reviewed;
    private Integer grade;
    private String graderUsername;

    /**
     * Creates a new grading record for a specific content item.
     *
     * @param contentId the ID of the post or reply
     */
    public GradingRecord(int contentId) {
        this.contentId = contentId;
        this.reviewed = false;
        this.grade = null;
        this.graderUsername = null;
    }

    /**
     * Returns the content ID.
     *
     * @return the content ID
     */
    public int getContentId() {
        return contentId;
    }

    /**
     * Returns whether the item has been reviewed.
     *
     * @return true if reviewed, false otherwise
     */
    public boolean isReviewed() {
        return reviewed;
    }

    /**
     * Sets the reviewed state.
     *
     * @param reviewed the new reviewed state
     */
    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    /**
     * Returns the assigned grade.
     *
     * @return the grade, or null if none exists
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * Sets the grade.
     *
     * @param grade the grade to store
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    /**
     * Returns the grader username.
     *
     * @return the grader username
     */
    public String getGraderUsername() {
        return graderUsername;
    }

    /**
     * Sets the grader username.
     *
     * @param graderUsername the grader username
     */
    public void setGraderUsername(String graderUsername) {
        this.graderUsername = graderUsername;
    }
}