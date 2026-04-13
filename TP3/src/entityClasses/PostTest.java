package entityClasses;

// Needed libraries of JUnit Testing
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * <p> Title: PostTest </p>
 *
 * <p> Description: A class under the entityClasses Package that contains code to test the implementation of the PostTest.java file. </p>
 * <p> Contains Tests to see whether the title, body, threads are validated before added to the database. </p>
 */
class PostTest {

	/**
	 * <p> Normal Test 1 </p>
	 * <p> Requirement Met </p>
	 * 
	 */
	@Test
	public void NormalTest1() {
		// post object is created and methods of the object is called for attribute validations
		Post post = new Post("Fauzan", "A", "I believe we are expected to complete all testing via JUnit?", "", "General");
		assertEquals(true, post.validateTitle());
	}

	/**
	 * <p> Normal Test 2 </p>
	 * <p> Requirement Met </p>
	 */
	@Test
	public void NormalTest2() {
		Post post = new Post("Agastya", "HW", "Can you explain what is Operational Aspects", "", "General");
		assertEquals(true, post.validateTitle());
	}

	/**
	 * <p> Normal Test 3 </p>
	 * <p> Requirement Met </p>
	 */
	@Test
	public void NormalTest3() {
		Post post = new Post("Nayef", "EC Deadline?", "Is extra credit due monday or wednesday?", "", "General");
		assertEquals(true, post.validateTitle());
	}

	/**
	 * <p> Normal Test 4 </p>
	 * <p> Requirement Met </p>
	 */
	@Test
	public void NormalTest4() {
		Post post = new Post("Hector", "Does anyone know if the TP3 aspects are finalized yet because I have not seen them posted on canvas and I was wondering about the release date", "Also another question in a bit", "", "General");
		assertEquals(true, post.validateTitle());
	}

	/**
	 * <p> Normal Test 5 </p>
	 * <p> Requirement Met </p>
	 */
	@Test
	public void NormalTest5() {
		Post post = new Post("Fauzan", "Does anyone know if the TP3 aspects are finalized yet because I have not seen them posted on canvas by the professor and I was wondering if anyone had info", "Not sure if I am wromg but as a group we are supposed to complete them", "", "General");
		assertEquals(true, post.validateTitle());
	}

	/**
	 * <p> Normal Test 6 </p>
	 * <p> Requirement Met </p>
	 */
	@Test
	public void NormalTest6() {
		Post post = new Post("Fauzan", "HW3 - JUnit Testing", "I believe we are expected to complete all testing via JUnit since it has been now taught in class. Is that correct?", "", "General");
		assertEquals(true, post.validateBody());
	}

	/**
	 * <p> Normal Test 7 </p>
	 * <p> Requirement Met </p>
	 */
	@Test
	public void NormalTest7() {
		Post post = new Post("Agastya", "Operational Aspects", "Can you explain what is Operational Aspects", "", null);
		assertEquals("General", post.getThreadName());
	}

	/**
	 * <p> Robust Test 1 - Requirements Not Met </p>
	 * <p> Error: No Title</p>
	 */
	@Test
	public void RobustTest1() {
		Post post = new Post("Fauzan", "", "I believe we are expected to complete all testing via JUnit since it has been now taught in class. Is that correct?", "", "General");
		assertEquals(false, post.validateTitle());
	}

	/**
	 * <p> Robust Test 1 - Requirements Not Met </p>
	 * <p> Error: Title too long </p>
	 */
	@Test
	public void RobustTest2() {
		Post post = new Post("Nayef", "Does anyone know if the TP3 aspects are finalized yet because I have not seen them posted on canvas by the professor and I was wondering if anyone had any information about the release date for the aspects document that we need for TP3!", "Is extra credit due monday or wednesday?", "", "General");
		assertEquals(false, post.validateTitle());
	}

	/**
	 * <p> Robust Test 1 - Requirements Not Met </p>
	 * <p> Error: title is null</p>
	 */
	@Test
	public void RobustTest3() {
		Post post = new Post("Fauzan", "HW3 - JUnit Testing", "I believe we are expected to complete all testing via JUnit since it has been now taught in class. Is that correct?", "", "General");
		post.setTitle(null);
		assertEquals(true, post.validateTitle());
	}

	/**
	 * <p> Robust Test 1 - Requirements Not Met </p>
	 * <p> Error: body is null</p>
	 */
	@Test
	public void RobustTest4() {
		Post post = new Post("Nayef", "EC Deadline for HW3", "Is extra credit due monday or wednesday?", "", "General");
		post.setBody(null);
		assertEquals(false, post.validateBody());
	}

	/**
	 * <p> Robust Test 1 - Requirements Not Met </p>
	 * <p> Error: title is empty</p>
	 */
	@Test
	public void RobustTest5() {
		Post post = new Post("Agastya", "   ", "Can you explain what is Operational Aspects", "", "General");
		assertEquals(false, post.validateTitle());
	}

}
