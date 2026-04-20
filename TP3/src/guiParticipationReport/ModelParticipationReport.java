package guiParticipationReport;

import entityClasses.Post;
import gradingTools.gradingStatistics;

import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Title: ModelParticipationReport Class </p>
 *
 * <p> Description: Provides the data-access and computation layer for the Student
 * Participation Report page.  All methods are static and operate on a provided list
 * of Post objects so they can be tested independently of the running GUI. </p>
 *
 * <p> This class supports User Story #7 – Student Participation Report Generation.
 * It aggregates data from the database, the gradingStatistics utility, and the
 * Post entity's feedback and flag fields (added for TP3). </p>
 *
 * <p> Tested by: ModelParticipationReportTest (JUnit 5) </p>
 *
 * @author Jack Ding
 * @version 1.00  2026-04-19  Initial implementation for TP3
 */
public class ModelParticipationReport {

    /*****
     * <p> Default constructor — not used; all methods are static. </p>
     */
    public ModelParticipationReport() {}

    /*****
     * <p> Loads every post and reply from the database. </p>
     *
     * @return the full list of posts and replies in the system
     */
    public static List<Post> loadAllPosts() {
        return applicationMain.FoundationsMain.database.getAllPosts();
    }

    /*****
     * <p> Returns a list of every unique username that has posted or replied. </p>
     *
     * <p> Delegates to gradingStatistics so the definition of "student" stays
     * consistent across the grading features. </p>
     *
     * @param allPosts the full list of posts to search
     * @return list of unique usernames, in the order they first appear
     */
    public static List<String> getStudentUsernames(List<Post> allPosts) {
        return gradingStatistics.getAllStudentUsernames(allPosts);
    }

    /*****
     * <p> Builds a one-line participation summary for the given student. </p>
     *
     * <p> Counts original posts (parentPostID == -1) and replies separately so
     * the grader can see both numbers at a glance.  The unique-author count and
     * threshold check are delegated to gradingStatistics to keep the logic
     * consistent with the Grading Statistics page. </p>
     *
     * @param username the student to summarise
     * @param allPosts the full list of posts to analyse
     * @return a formatted summary string, e.g.
     *         "Posts: 2   |   Replies: 4   |   Unique Authors Replied To: 3   |   Threshold Met: Yes"
     */
    public static String getSummaryStats(String username, List<Post> allPosts) {
        int postCount  = 0;
        int replyCount = 0;

        for (Post p : allPosts) {
            if (!p.getUsername().equals(username) || p.isDeleted()) continue;
            if (p.getParentPostID() == -1) postCount++;
            else                           replyCount++;
        }

        int  uniqueAuthors = gradingStatistics.countUniqueAuthors(username, allPosts);
        boolean thresholdMet = gradingStatistics.minimumRequirement(uniqueAuthors);

        return "Posts: "  + postCount  +
               "   |   Replies: " + replyCount +
               "   |   Unique Authors Replied To: " + uniqueAuthors +
               "   |   Threshold Met: " + (thresholdMet ? "Yes" : "No");
    }

    /*****
     * <p> Builds a list of formatted detail strings for every post and reply
     * belonging to the given student, including flag and feedback status. </p>
     *
     * <p> Each entry is one line so it displays cleanly inside a ListView.
     * Deleted posts are skipped because they are no longer relevant to grading. </p>
     *
     * <p> Format examples: </p>
     * <ul>
     *   <li> [POST] "My Question"  |  Flagged: No  |  Feedback: None </li>
     *   <li> [REPLY to alice] "Great point..."  |  Flagged: YES - off topic  |  Feedback: None </li>
     *   <li> [POST] "Hello"  |  Flagged: No  |  Feedback from grader: Well written </li>
     * </ul>
     *
     * @param username the student whose content to list
     * @param allPosts the full list of posts to search
     * @return list of formatted strings ready for display in a ListView
     */
    public static List<String> getPostDetails(String username, List<Post> allPosts) {
        List<String> details = new ArrayList<>();

        for (Post post : allPosts) {
            if (!post.getUsername().equals(username) || post.isDeleted()) continue;

            // Build the type and title portion of the entry
            String typeAndTitle;
            if (post.getParentPostID() == -1) {
                typeAndTitle = "[POST] \"" + post.getTitle() + "\"";
            } else {
                // For replies, show who the student was replying to
                String parentAuthor = findParentAuthor(post.getParentPostID(), allPosts);
                typeAndTitle = "[REPLY to " + parentAuthor + "] \"" + post.getBody() + "\"";
            }

            // Build the flag portion
            String flagInfo;
            if (post.isFlagged()) {
                String reason = post.getReason();
                flagInfo = "Flagged: YES" + (reason != null && !reason.isBlank() ? " - " + reason : "");
            } else {
                flagInfo = "Flagged: No";
            }

            // Build the feedback portion
            String feedbackInfo;
            String fb = post.getFeedback();
            if (fb == null || fb.isBlank()) {
                feedbackInfo = "Feedback: None";
            } else {
                feedbackInfo = "Feedback from " + post.getFeedbackAuthor() + ": " + fb;
            }

            details.add(typeAndTitle + "   |   " + flagInfo + "   |   " + feedbackInfo);
        }

        return details;
    }

    /*****
     * <p> Finds the username of the author who wrote a given post ID. </p>
     *
     * <p> Used to label reply entries with "REPLY to [author]" rather than a raw ID,
     * which makes the report readable without needing to cross-reference the posts table. </p>
     *
     * @param parentPostID the ID of the parent post to look up
     * @param allPosts     the full list of posts to search
     * @return the parent author's username, or "unknown" if the post was not found
     */
    private static String findParentAuthor(int parentPostID, List<Post> allPosts) {
        for (Post p : allPosts) {
            if (p.getPostID() == parentPostID) return p.getUsername();
        }
        return "unknown";
    }

}
