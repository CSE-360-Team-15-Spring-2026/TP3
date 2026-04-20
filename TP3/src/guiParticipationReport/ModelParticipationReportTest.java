package guiParticipationReport;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import entityClasses.Post;

import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Title: ModelParticipationReportTest Class </p>
 *
 * <p> Description: JUnit 5 tests for ModelParticipationReport.  Tests operate on
 * hand-built Post lists so no database connection is required.  Covers
 * getSummaryStats (post/reply counts, threshold boundary, deleted exclusion) and
 * getPostDetails (flag/feedback formatting, reply labelling, deleted exclusion,
 * missing parent fallback). </p>
 *
 * <p> Tested class: guiParticipationReport.ModelParticipationReport </p>
 *
 * @author Jack Ding
 * @version 1.00  2026-04-19  Initial implementation for TP3
 */
class ModelParticipationReportTest {

    /** Creates a top-level post (parentPostID defaults to -1). */
    private static Post makePost(int id, String username, String title, String body) {
        Post p = new Post(username, title, body, "", "General");
        p.setPostID(id);
        return p;
    }

    /** Creates a reply (parentPostID set to parentId). */
    private static Post makeReply(int id, String username, String body, int parentId) {
        Post p = new Post(username, "", body, "", "General");
        p.setPostID(id);
        p.setParentPostID(parentId);
        return p;
    }

    // getSummaryStats

    /**
     * <p> Verifies the summary string format and that posts and replies are counted
     * separately.  With only one unique author replied to, threshold is not met. </p>
     */
    @Test
    void getSummaryStats_countsPostsAndReplies() {
        List<Post> posts = new ArrayList<>();
        posts.add(makePost(1, "alice", "My Question", "Hello class"));
        posts.add(makePost(2, "bob",   "Bob Post",    "Bob content"));
        posts.add(makeReply(3, "alice", "Good point.", 2));

        String result = ModelParticipationReport.getSummaryStats("alice", posts);

        assertEquals(
            "Posts: 1   |   Replies: 1   |   Unique Authors Replied To: 1   |   Threshold Met: No",
            result);
    }

    /**
     * <p> Verifies that threshold is reported as Yes when the student has replied
     * to exactly three unique authors (the minimum boundary). </p>
     */
    @Test
    void getSummaryStats_thresholdMetAtThreeUniqueAuthors() {
        List<Post> posts = new ArrayList<>();
        posts.add(makePost(1, "bob",   "B", "b"));
        posts.add(makePost(2, "carol", "C", "c"));
        posts.add(makePost(3, "dave",  "D", "d"));
        posts.add(makeReply(4, "alice", "to bob",   1));
        posts.add(makeReply(5, "alice", "to carol", 2));
        posts.add(makeReply(6, "alice", "to dave",  3));

        String result = ModelParticipationReport.getSummaryStats("alice", posts);

        assertEquals(
            "Posts: 0   |   Replies: 3   |   Unique Authors Replied To: 3   |   Threshold Met: Yes",
            result);
    }

    /**
     * <p> Verifies that deleted posts and replies are not included in the counts. </p>
     */
    @Test
    void getSummaryStats_excludesDeletedPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(makePost(1, "alice", "Active Post", "here"));

        Post deleted = makePost(2, "alice", "Deleted Post", "gone");
        deleted.setDeleted(true);
        posts.add(deleted);

        String result = ModelParticipationReport.getSummaryStats("alice", posts);

        assertEquals(
            "Posts: 1   |   Replies: 0   |   Unique Authors Replied To: 0   |   Threshold Met: No",
            result);
    }

    /**
     * <p> Verifies that an empty post list returns all-zero counts and threshold not met. </p>
     */
    @Test
    void getSummaryStats_emptyList() {
        String result = ModelParticipationReport.getSummaryStats("alice", new ArrayList<>());

        assertEquals(
            "Posts: 0   |   Replies: 0   |   Unique Authors Replied To: 0   |   Threshold Met: No",
            result);
    }

    // getPostDetails 

    /**
     * <p> Verifies the base-case format for a post with no flag and no feedback. </p>
     */
    @Test
    void getPostDetails_plainPost() {
        List<Post> posts = new ArrayList<>();
        posts.add(makePost(1, "alice", "Hello World", "My first post"));

        List<String> details = ModelParticipationReport.getPostDetails("alice", posts);

        assertEquals(1, details.size());
        assertEquals(
            "[POST] \"Hello World\"   |   Flagged: No   |   Feedback: None",
            details.get(0));
    }

    /**
     * <p> Verifies that a flagged post with a reason shows "Flagged: YES - reason". </p>
     */
    @Test
    void getPostDetails_flaggedWithReason() {
        List<Post> posts = new ArrayList<>();
        Post post = makePost(1, "alice", "Bad Post", "content");
        post.setFlagged(true);
        post.setReason("off topic");
        posts.add(post);

        List<String> details = ModelParticipationReport.getPostDetails("alice", posts);

        assertEquals(1, details.size());
        assertEquals(
            "[POST] \"Bad Post\"   |   Flagged: YES - off topic   |   Feedback: None",
            details.get(0));
    }

    /**
     * <p> Verifies that grader feedback is shown as "Feedback from author: text". </p>
     */
    @Test
    void getPostDetails_withFeedback() {
        List<Post> posts = new ArrayList<>();
        Post post = makePost(1, "alice", "Good Post", "content");
        post.setFeedback("Well written");
        post.setFeedbackAuthor("grader1");
        posts.add(post);

        List<String> details = ModelParticipationReport.getPostDetails("alice", posts);

        assertEquals(1, details.size());
        assertEquals(
            "[POST] \"Good Post\"   |   Flagged: No   |   Feedback from grader1: Well written",
            details.get(0));
    }

    /**
     * <p> Verifies that a reply entry shows the parent post's author name. </p>
     */
    @Test
    void getPostDetails_replyShowsParentAuthor() {
        List<Post> posts = new ArrayList<>();
        posts.add(makePost(1, "bob", "Bob Question", "What is TP3?"));
        posts.add(makeReply(2, "alice", "TP3 is the third project phase.", 1));

        List<String> details = ModelParticipationReport.getPostDetails("alice", posts);

        assertEquals(1, details.size());
        assertTrue(details.get(0).startsWith("[REPLY to bob]"));
    }

    /**
     * <p> Verifies that deleted posts are skipped and do not appear in the output. </p>
     */
    @Test
    void getPostDetails_deletedPostIsSkipped() {
        List<Post> posts = new ArrayList<>();
        Post deleted = makePost(1, "alice", "Old Post", "content");
        deleted.setDeleted(true);
        posts.add(deleted);

        List<String> details = ModelParticipationReport.getPostDetails("alice", posts);

        assertTrue(details.isEmpty());
    }

    /**
     * <p> Verifies that when a reply's parent post is not in the list, the label
     * falls back to "unknown". </p>
     */
    @Test
    void getPostDetails_missingParentShowsUnknown() {
        List<Post> posts = new ArrayList<>();
        posts.add(makeReply(1, "alice", "Orphaned reply", 99));

        List<String> details = ModelParticipationReport.getPostDetails("alice", posts);

        assertEquals(1, details.size());
        assertTrue(details.get(0).startsWith("[REPLY to unknown]"));
    }
}
