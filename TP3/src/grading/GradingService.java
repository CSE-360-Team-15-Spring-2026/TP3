package grading;

import java.util.HashMap;
import java.util.Map;

/*******
 * <p> Title: GradingService Class. </p>
 *
 * <p> Description: Prototype service for marking discussion content as reviewed and assigning
 * grade points. This class supports the instructional-team aspect of marking content as
 * reviewed and assigning grade points.
 *
 * Each grading action is traceable to the grader who performed it, and grading is enforced
 * to occur only after a review has taken place. </p>
 */
public class GradingService {

    /** In-memory store mapping content IDs to their associated grading records. */
    private final Map<Integer, GradingRecord> records = new HashMap<>();


    /*-*******************************************************************************************

    Constructor

     */

    /**
     * Default constructor is not used (all methods are instance-based).
     */
    public GradingService() {}


    /*-*******************************************************************************************

    Grading Actions

     */

    /**
     * <p> Marks a discussion item as reviewed by an authorized grader. </p>
     *
     * @param contentId      the ID of the content item to mark as reviewed
     * @param graderUsername the username of the grader performing the review
     * @return true if the review succeeded, false otherwise
     */
    public boolean markAsReviewed(int contentId, String graderUsername) {
        if (contentId <= 0) {
            return false;
        }

        if (!isAuthorizedGrader(graderUsername)) {
            return false;
        }

        GradingRecord record = records.computeIfAbsent(contentId, GradingRecord::new);

        // Save the reviewer identity so the grading action is traceable.
        record.setReviewed(true);
        record.setGraderUsername(graderUsername);
        return true;
    }

    /**
     * <p> Assigns a grade to a content item that has already been reviewed. </p>
     *
     * @param contentId      the ID of the content item to grade
     * @param grade          the grade to assign
     * @param graderUsername the username of the grader assigning the grade
     * @return true if grading succeeded, false otherwise
     */
    public boolean assignGrade(int contentId, Integer grade, String graderUsername) {
        if (contentId <= 0) {
            return false;
        }

        if (grade == null || grade < 0) {
            return false;
        }

        if (!isAuthorizedGrader(graderUsername)) {
            return false;
        }

        GradingRecord record = records.get(contentId);

        // Grading must not happen before review because that would break the workflow.
        if (record == null || !record.isReviewed()) {
            return false;
        }

        record.setGrade(grade);
        record.setGraderUsername(graderUsername);
        return true;
    }

    /**
     * <p> Updates an existing grade for a content item that has already been reviewed
     * and graded. </p>
     *
     * @param contentId      the ID of the content item whose grade is being updated
     * @param newGrade       the new grade value to store
     * @param graderUsername the username of the grader making the change
     * @return true if the grade update succeeded, false otherwise
     */
    public boolean updateGrade(int contentId, Integer newGrade, String graderUsername) {
        if (contentId <= 0) {
            return false;
        }

        if (newGrade == null || newGrade < 0) {
            return false;
        }

        if (!isAuthorizedGrader(graderUsername)) {
            return false;
        }

        GradingRecord record = records.get(contentId);

        if (record == null || !record.isReviewed() || record.getGrade() == null) {
            return false;
        }

        record.setGrade(newGrade);
        record.setGraderUsername(graderUsername);
        return true;
    }


    /*-*******************************************************************************************

    Getters

     */

    /**
     * <p> Returns whether a content item has been marked as reviewed. </p>
     *
     * @param contentId the content ID to check
     * @return true if reviewed, false otherwise
     */
    public boolean isReviewed(int contentId) {
        GradingRecord record = records.get(contentId);
        return record != null && record.isReviewed();
    }

    /**
     * <p> Returns the stored grade for a content item. </p>
     *
     * @param contentId the content ID to look up
     * @return the grade, or null if none exists
     */
    public Integer getGrade(int contentId) {
        GradingRecord record = records.get(contentId);
        return record == null ? null : record.getGrade();
    }

    /**
     * <p> Returns the full grading record for a content item. </p>
     *
     * @param contentId the content ID to look up
     * @return the grading record, or null if none exists
     */
    public GradingRecord getRecord(int contentId) {
        return records.get(contentId);
    }


    /*-*******************************************************************************************

    Private helpers

     */

    /**
     * <p> Checks whether a user is an authorized grader. For this prototype, usernames
     * beginning with "staff_" or "grader_" represent instructional-team members. </p>
     *
     * @param username the username to validate
     * @return true if authorized, false otherwise
     */
    private boolean isAuthorizedGrader(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }

        // For this prototype, staff_ and grader_ usernames represent instructional-team users.
        return username.startsWith("staff_") || username.startsWith("grader_");
    }
}
