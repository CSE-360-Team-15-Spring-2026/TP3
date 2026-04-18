package grading;

import java.util.HashMap;
import java.util.Map;

/**
 * Prototype service for marking discussion content as reviewed
 * and assigning grade points.
 *
 * This class supports the instructional-team aspect
 * "Mark as Reviewed and Assign Grade Points."
 *
 */
public class GradingService {

    private final Map<Integer, GradingRecord> records = new HashMap<>();

    /**
     * Marks a discussion item as reviewed.
     *
     * @param contentId the ID of the content item
     * @param graderUsername the grader performing the review
     * @return true if review succeeded, false otherwise
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
     * Assigns a grade to a reviewed item.
     *
     * @param contentId the ID of the content item
     * @param grade the grade to assign
     * @param graderUsername the grader assigning the grade
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
     * Updates an existing grade.
     *
     * @param contentId the ID of the content item
     * @param newGrade the new grade value
     * @param graderUsername the grader making the change
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

    /**
     * Returns whether a content item has been reviewed.
     *
     * @param contentId the content ID
     * @return true if reviewed, false otherwise
     */
    public boolean isReviewed(int contentId) {
        GradingRecord record = records.get(contentId);
        return record != null && record.isReviewed();
    }

    /**
     * Returns the stored grade for a content item.
     *
     * @param contentId the content ID
     * @return the grade, or null if none exists
     */
    public Integer getGrade(int contentId) {
        GradingRecord record = records.get(contentId);
        return record == null ? null : record.getGrade();
    }

    /**
     * Returns the grading record for a content item.
     *
     * @param contentId the content ID
     * @return the grading record, or null if none exists
     */
    public GradingRecord getRecord(int contentId) {
        return records.get(contentId);
    }

    /**
     * Checks whether a user is an authorized grader.
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
