package gradingTools;

// Needed libraries for JUnit Testing
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

// Needed the objects created from HW2
import entityClasses.Post;
import entityClasses.Reply;

// Use of ArrayList to store posts and replies
import java.util.ArrayList;
import java.util.List;

/**
 * <p> Title: gradingStatisticsTest </p>
 *
 * <p> Description: A class under the gradingTools Package that contains code to test the implementation of the gradingStatistics.java file. </p>
 * <p> Contains Tests to verify whether the requirements for students are met or whether the posts and replies are calculated properly.  </p>
 */
class gradingStatisticsTest {

	/**
	 * <p> Normal Test 1 </p>
	 * <p> Test to get a return of 0 since no replies </p>
	 */
	@Test
	void NormalTest1() {
		List<Post> posts = new ArrayList<>();
		
		Post post = new Post("Fauzan", "HW3 - JUnit Testing", "I believe we are expected to complete all testing via JUnit since it has been now taught in class. Is that correct?", "", "General");
		post.setPostID(1);
		posts.add(post);
		
		assertEquals(0, gradingStatistics.countUniqueAuthors("Fauzan", posts));
	}
	
	/**
	 * <p> Normal Test 2 </p>
	 * <p> Test to get a return of 1 since Fauzan replied to Agastya's post </p>
	 */
	@Test
	void NormalTest2() {
		List<Post> posts = new ArrayList<>();
		
				Post agastyaPost = new Post("Agastya", "Operational Aspects", "Can you explain what is Operational Aspects", "", "General");
		agastyaPost.setPostID(1);
		posts.add(agastyaPost);
		
				Reply fauzanReply = new Reply(1, "Fauzan", "Operational aspects are high level things the Staff needs to do with the system that are not CRUD operations.");
		fauzanReply.setPostID(2);
		posts.add(fauzanReply);
		
				assertEquals(1, gradingStatistics.countUniqueAuthors("Fauzan", posts));
	}
	
	/**
	 * <p> Normal Test 3 </p>
	 * <p> Test to get a return of 1 since Fauzan replied to Agastya's post multiple times but unique authors is still 1 </p>
	 */
	@Test
	public void NormalTest3() {
		List<Post> posts = new ArrayList<>();
		
		Post agastyaPost = new Post("Agastya", "Operational Aspects", "Can you explain what is Operational Aspects", "", "General");
		agastyaPost.setPostID(1);
		posts.add(agastyaPost);
		
		Reply reply1 = new Reply(1, "Fauzan", "Operational aspects are high level things the Staff needs to do.");
		reply1.setPostID(2);
		posts.add(reply1);
		
		Reply reply2 = new Reply(1, "Fauzan", "They are not CRUD operations like creating or editing posts.");
		reply2.setPostID(3);
		posts.add(reply2);
		
		Reply reply3 = new Reply(1, "Fauzan", "Basically, it needs to be stuff the instructor would use for grading students posts and replies");
		reply3.setPostID(4);
		posts.add(reply3);
		
		assertEquals(1, gradingStatistics.countUniqueAuthors("Fauzan", posts));
	}
	
	/**
	 * <p> Normal Test 4 </p>
	 * <p> Test to get a return of 2 since Fauzan replied to both Agastya's and Nayef's posts </p>
	 */
	@Test
	public void NormalTest4() {
		List<Post> posts = new ArrayList<>();
		
		Post agastyaPost = new Post("Agastya", "Operational Aspects", "Can you explain what is Operational Aspects", "", "General");
		agastyaPost.setPostID(1);
		posts.add(agastyaPost);
		
		Post nayefPost = new Post("Nayef", "EC Deadline for HW3", "Is extra credit due monday or wednesday?", "", "General");
		nayefPost.setPostID(2);
		posts.add(nayefPost);
		
		Reply replyToAgastya = new Reply(1, "Fauzan", "Operational aspects are the grading items the staff needs to do.");
		replyToAgastya.setPostID(3);
		posts.add(replyToAgastya);
		
		Reply replyToNayef = new Reply(2, "Fauzan", "I think the extra credit is due Monday");
		replyToNayef.setPostID(4);
		posts.add(replyToNayef);
		
		assertEquals(2, gradingStatistics.countUniqueAuthors("Fauzan", posts));
	}
	
	/**
	 * <p> Normal Test 5 </p>
	 * <p> Test to get a return of 2 since Agastya replied to Fauzan and Nayef but self replies are excluded </p>
	 */
	@Test
	public void NormalTest5() {
		List<Post> posts = new ArrayList<>();
		
		Post fauzanPost = new Post("Fauzan", "HW3 - JUnit Testing", "I believe we are expected to complete all testing via JUnit since it has been now taught in class. Is that correct?", "", "General");
		fauzanPost.setPostID(1);
		posts.add(fauzanPost);
		
		Post agastyaPost = new Post("Agastya", "Operational Aspects", "Can you explain what is Operational Aspects", "", "General");
		agastyaPost.setPostID(2);
		posts.add(agastyaPost);
		
		Post nayefPost = new Post("Nayef", "EC Deadline for HW3", "Is extra credit due monday or wednesday?", "", "General");
		nayefPost.setPostID(3);
		posts.add(nayefPost);
		
		Reply replyToFauzan = new Reply(1, "Agastya", "Yes JUnit is required");
		replyToFauzan.setPostID(4);
		posts.add(replyToFauzan);
		
		Reply replyToSelf = new Reply(2, "Agastya", "Nevermind, I understood it.");
		replyToSelf.setPostID(5);
		posts.add(replyToSelf);
		
		Reply replyToNayef = new Reply(3, "Agastya", "I believe the extra credit deadline is on Monday");
		replyToNayef.setPostID(6);
		posts.add(replyToNayef);
		
		assertEquals(2, gradingStatistics.countUniqueAuthors("Agastya", posts));
	}
	
	/**
	 * <p> Normal Test 6 </p>
	 * <p> Test to get a return of 3 since Fauzan replied to Agastya, Nayef, and Hector's posts </p>
	 */
	@Test
	public void NormalTest6() {
		List<Post> posts = new ArrayList<>();
		
		// Posts is Created, assigned the PostID and added to the list
		Post agastyaPost = new Post("Agastya", "Operational Aspects", "Can you explain what is Operational Aspects", "", "General");
		agastyaPost.setPostID(1);
		posts.add(agastyaPost);
		
		Post nayefPost = new Post("Nayef", "EC Deadline for HW3", "Is extra credit due monday or wednesday?", "", "General");
		nayefPost.setPostID(2);
		posts.add(nayefPost);
		
		Post hectorPost = new Post("Hector", "HW3 - JUnit Testing", "I believe we are expected to complete all testing via JUnit since it has been now taught in class. Is that correct?", "", "General");
		hectorPost.setPostID(3);
		posts.add(hectorPost);
		
		// Replies are created, postID is assigned and the reply is added to the list
		// Replies are linked to post via parentPostID
		Reply replyToAgastya = new Reply(1, "Fauzan", "Operational aspects are the grading items the staff team needs.");
		replyToAgastya.setPostID(4);
		posts.add(replyToAgastya);
		
		Reply replyToNayef = new Reply(2, "Fauzan", "Extra credit is due monday, the main deadline is wednesday.");
		replyToNayef.setPostID(5);
		posts.add(replyToNayef);
		
		Reply replyToHector = new Reply(3, "Fauzan", "Yes JUnit is required");
		replyToHector.setPostID(6);
		posts.add(replyToHector);
		
		// assert to verify whether the test case works as expected
		assertEquals(3, gradingStatistics.countUniqueAuthors("Fauzan", posts));
	}
	
	/**
	 * <p> Normal Test 7 </p>
	 * <p> Test to get a return of 0 since Fauzan only replied to their own post </p>
	 */
	@Test
	public void NormalTest7() {
		List<Post> posts = new ArrayList<>();
		
		Post fauzanPost = new Post("Fauzan", "HW3 - JUnit Testing", "I believe we are expected to complete all testing via JUnit since it has been now taught in class. Is that correct?", "", "General");
		fauzanPost.setPostID(1);
		posts.add(fauzanPost);
		
				Reply selfReply = new Reply(1, "Fauzan", "Never mind I watched the JUnit video and figured it out.");
		selfReply.setPostID(2);
		posts.add(selfReply);
		
		assertEquals(0, gradingStatistics.countUniqueAuthors("Fauzan", posts));
	}
	
	/**
	 * <p> Normal Test 8 </p>
	 * <p> Test to get a return of 1 since Fauzan's reply to Nayef is deleted and should not be counted </p>
	 */
	@Test
	public void NormalTest8() {
		List<Post> posts = new ArrayList<>();
		
		Post agastyaPost = new Post("Agastya", "Operational Aspects", "Can you explain what is Operational Aspects", "", "General");
		agastyaPost.setPostID(1);
		posts.add(agastyaPost);
		
		Post nayefPost = new Post("Nayef", "EC Deadline for HW3", "Is extra credit due monday or wednesday?", "", "General");
		nayefPost.setPostID(2);
		posts.add(nayefPost);
		
		Reply replyToAgastya = new Reply(1, "Fauzan", "Operational aspects are things the instructor needs for grading.");
		replyToAgastya.setPostID(3);
		posts.add(replyToAgastya);
		
				Reply replyToNayef = new Reply(2, "Fauzan", "Extra credit is due monday.");
		replyToNayef.setPostID(4);
		replyToNayef.setDeleted(true);
		posts.add(replyToNayef);
		
				assertEquals(1, gradingStatistics.countUniqueAuthors("Fauzan", posts));
	}
	
	/**
	 * <p> Normal Test 9 </p>
	 * <p> Test to confirm minimumRequirement returns true when unique author count is exactly 3 </p>
	 */
	@Test
	public void NormalTest9() {
		assertEquals(true, gradingStatistics.minimumRequirement(3));
	}
	
	/**
	 * <p> Normal Test 10 </p>
	 * <p> Test to confirm minimumRequirement returns false when unique author count is below 3 </p>
	 */
	@Test
	public void NormalTest10() {
		assertEquals(false, gradingStatistics.minimumRequirement(2));
	}
	
	/**
	 * <p> Normal Test 11 </p>
	 * <p> Test to get a return of 4 since Fauzan replied to Agastya, Nayef, Hector, and Jack's posts </p>
	 */
	@Test
	public void NormalTest11() {
	    List<Post> posts = new ArrayList<>();

	    Post agastyaPost = new Post("Agastya", "Operational Aspects", "Can you explain what is Operational Aspects", "", "General");
	    agastyaPost.setPostID(1);
	    posts.add(agastyaPost);

	    Post nayefPost = new Post("Nayef", "EC Deadline for HW3", "Is extra credit due monday or wednesday?", "", "General");
	    nayefPost.setPostID(2);
	    posts.add(nayefPost);

	    Post hectorPost = new Post("Hector", "HW3 - JUnit Testing", "Is JUnit required for all tests?", "", "General");
	    hectorPost.setPostID(3);
	    posts.add(hectorPost);

	    Post jackPost = new Post("Jack", "TP3 Aspects", "Has anyone seen the TP3 aspects document yet?", "", "General");
	    jackPost.setPostID(4);
	    posts.add(jackPost);

	    Reply r1 = new Reply(1, "Fauzan", "Operational aspects are high level grading tasks.");
	    r1.setPostID(5);
	    posts.add(r1);

	    Reply r2 = new Reply(2, "Fauzan", "Extra credit is due Monday.");
	    r2.setPostID(6);
	    posts.add(r2);

	    Reply r3 = new Reply(3, "Fauzan", "Yes JUnit is required.");
	    r3.setPostID(7);
	    posts.add(r3);

	    Reply r4 = new Reply(4, "Fauzan", "I have not seen it posted yet either.");
	    r4.setPostID(8);
	    posts.add(r4);

	    assertEquals(4, gradingStatistics.countUniqueAuthors("Fauzan", posts));
	}
	
	/**
	 * <p> Normal Test 12 </p>
	 * <p> Test to confirm minimumRequirement returns false when unique author count is 0 </p>
	 */
	@Test
	public void NormalTest12() {
	    assertEquals(false, gradingStatistics.minimumRequirement(0));
	}
	
	/**
	 * <p> Robust Test 1 </p>
	 * <p> Test to get a return of 0 when a null username is passed in </p>
	 */
	@Test
	public void RobustTest1() {
		List<Post> posts = new ArrayList<>();
		
		Post nayefPost = new Post("Nayef", "EC Deadline for HW3", "Is extra credit due monday or wednesday?", "", "General");
		nayefPost.setPostID(1);
		posts.add(nayefPost);
		
				assertEquals(0, gradingStatistics.countUniqueAuthors(null, posts));
	}
	
	/**
	 * <p> Robust Test 2 </p>
	 * <p> Test to get a return of 0 when an empty post list is passed in </p>
	 */
	@Test
	public void RobustTest2() {
		List<Post> posts = new ArrayList<>();
		
		assertEquals(0, gradingStatistics.countUniqueAuthors("Fauzan", posts));
	}
	
	/**
	 * <p> Robust Test 3 </p>
	 * <p> Test to get a return of 0 when an empty string username is passed in </p>
	 */
	@Test
	public void RobustTest3() {
	    List<Post> posts = new ArrayList<>();

	    Post nayefPost = new Post("Nayef", "EC Deadline for HW3", "Is extra credit due monday or wednesday?", "", "General");
	    nayefPost.setPostID(1);
	    posts.add(nayefPost);

	    assertEquals(0, gradingStatistics.countUniqueAuthors("", posts));
	}

}
