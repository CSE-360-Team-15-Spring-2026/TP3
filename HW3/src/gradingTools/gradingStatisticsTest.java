package gradingTools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import entityClasses.Post;
import entityClasses.Reply;

import java.util.ArrayList;
import java.util.List;

class gradingStatisticsTest {

	@Test
	void NormalTest1() {
		List<Post> posts = new ArrayList<>();
		
		Post post = new Post("Fauzan", "HW3 - JUnit Testing", "I believe we are expected to complete all testing via JUnit since it has been now taught in class. Is that correct?", "", "General");
		post.setPostID(1);
		posts.add(post);
		
		assertEquals(0, gradingStatistics.countUniqueAuthors("Fauzan", posts));
	}
	
	@Test
	void NormalTest2() {
		List<Post> posts = new ArrayList<>();
		
				Post agastyaPost = new Post("Agastya", "Operational Aspects", "Can you explain what is Operational Aspects", "", "General");
		agastyaPost.setPostID(1);
		posts.add(agastyaPost);
		
				Reply fauzanReply = new Reply(1, "Fauzan", "Operational aspects are high level things the instructional team needs to do with the system that are not basic CRUD operations.");
		fauzanReply.setPostID(2);
		posts.add(fauzanReply);
		
				assertEquals(1, gradingStatistics.countUniqueAuthors("Fauzan", posts));
	}
	
	@Test
	public void NormalTest3() {
		List<Post> posts = new ArrayList<>();
		
		Post agastyaPost = new Post("Agastya", "Operational Aspects", "Can you explain what is Operational Aspects", "", "General");
		agastyaPost.setPostID(1);
		posts.add(agastyaPost);
		
		Reply reply1 = new Reply(1, "Fauzan", "Operational aspects are high level things the instructional team needs to do.");
		reply1.setPostID(2);
		posts.add(reply1);
		
		Reply reply2 = new Reply(1, "Fauzan", "They are not basic CRUD operations like creating or editing posts.");
		reply2.setPostID(3);
		posts.add(reply2);
		
		Reply reply3 = new Reply(1, "Fauzan", "Basically, it needs to be stuff the instructor would use for grading students posts and replies");
		reply3.setPostID(4);
		posts.add(reply3);
		
		assertEquals(1, gradingStatistics.countUniqueAuthors("Fauzan", posts));
	}
	
	@Test
	public void NormalTest4() {
		List<Post> posts = new ArrayList<>();
		
		Post agastyaPost = new Post("Agastya", "Operational Aspects", "Can you explain what is Operational Aspects", "", "General");
		agastyaPost.setPostID(1);
		posts.add(agastyaPost);
		
		Post nayefPost = new Post("Nayef", "EC Deadline for HW3", "Is extra credit due monday or wednesday?", "", "General");
		nayefPost.setPostID(2);
		posts.add(nayefPost);
		
		Reply replyToAgastya = new Reply(1, "Fauzan", "Operational aspects are the grading workflow things the instructor needs to do.");
		replyToAgastya.setPostID(3);
		posts.add(replyToAgastya);
		
		Reply replyToNayef = new Reply(2, "Fauzan", "I think the extra credit is due Monday");
		replyToNayef.setPostID(4);
		posts.add(replyToNayef);
		
		assertEquals(2, gradingStatistics.countUniqueAuthors("Fauzan", posts));
	}
	
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
	
	@Test
	public void NormalTest6() {
		List<Post> posts = new ArrayList<>();
		
		Post agastyaPost = new Post("Agastya", "Operational Aspects", "Can you explain what is Operational Aspects", "", "General");
		agastyaPost.setPostID(1);
		posts.add(agastyaPost);
		
		Post nayefPost = new Post("Nayef", "EC Deadline for HW3", "Is extra credit due monday or wednesday?", "", "General");
		nayefPost.setPostID(2);
		posts.add(nayefPost);
		
		Post hectorPost = new Post("Hector", "HW3 - JUnit Testing", "I believe we are expected to complete all testing via JUnit since it has been now taught in class. Is that correct?", "", "General");
		hectorPost.setPostID(3);
		posts.add(hectorPost);
		
		Reply replyToAgastya = new Reply(1, "Fauzan", "Operational aspects are the grading workflow the instructional team needs.");
		replyToAgastya.setPostID(4);
		posts.add(replyToAgastya);
		
		Reply replyToNayef = new Reply(2, "Fauzan", "Extra credit is due monday, the main deadline is wednesday.");
		replyToNayef.setPostID(5);
		posts.add(replyToNayef);
		
		Reply replyToHector = new Reply(3, "Fauzan", "Yes JUnit is required");
		replyToHector.setPostID(6);
		posts.add(replyToHector);
		
		assertEquals(3, gradingStatistics.countUniqueAuthors("Fauzan", posts));
	}
	
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
	
	@Test
	public void NormalTest9() {
		assertEquals(true, gradingStatistics.minimumRequirement(3));
	}
	
	@Test
	public void NormalTest10() {
		assertEquals(false, gradingStatistics.minimumRequirement(2));
	}
	
	@Test
	public void RobustTest11() {
		List<Post> posts = new ArrayList<>();
		
		Post nayefPost = new Post("Nayef", "EC Deadline for HW3", "Is extra credit due monday or wednesday?", "", "General");
		nayefPost.setPostID(1);
		posts.add(nayefPost);
		
				assertEquals(0, gradingStatistics.countUniqueAuthors(null, posts));
	}
	
	@Test
	public void RobustTest12() {
		List<Post> posts = new ArrayList<>();
		
		assertEquals(0, gradingStatistics.countUniqueAuthors("Fauzan", posts));
	}

}
