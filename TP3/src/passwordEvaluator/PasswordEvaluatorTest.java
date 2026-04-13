package passwordEvaluator;

// Needed this for JUnit Testing
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


/**
 * <p> Title: PasswordEvaluatorTest </p>
 *
 * <p> Description: A class under the passwordEvaluator Package that contains code to test the implementation of the passwordEvaluator.java file. </p>
 * <p> Contains Tests to see whether valid passwords are accepted and whether invalid password are rejected by the system</p>
 */
class PasswordEvaluatorTest {

	/**
	 * <p> Normal Test 1: Requirements Met </p>
	 * <p> String Tested: !@Cse360* </p>
	 */
	@Test
	public void NormalTest1() {
		// Just pass the password as a parameter in evaluatePassword to test whether it follows the requirements
		assertEquals("", PasswordEvaluator.evaluatePassword("!@Cse360*"));
	}

	/**
	 * <p> Normal Test 2: Requirements Met </p>
	 * <p> String Tested: !@Cse360*a </p>
	 */
	@Test
	public void NormalTest2() {
		assertEquals("", PasswordEvaluator.evaluatePassword("!@Cse360*a"));
	}

	/**
	 * <p> Normal Test 3: Requirements Met </p>
	 * <p> String Tested: !@Cse360*abcdefgh </p>
	 */
	@Test
	public void NormalTest3() {
		assertEquals("", PasswordEvaluator.evaluatePassword("!@Cse360*abcdefgh"));
	}

	/**
	 * <p> Normal Test 4: Requirements Met </p>
	 * <p> String Tested: !@Cse360*abcdefghhhhhhhh </p>
	 */
	@Test
	public void NormalTest4() {
		assertEquals("", PasswordEvaluator.evaluatePassword("!@Cse360*abcdefghhhhhhhh"));
	}

	/**
	 * <p> Normal Test 5: Requirements Met</p>
	 * <p> String Tested: !@Cse360*aabcdefghhhhhhhh </p>
	 */
	@Test
	public void NormalTest5() {
		assertEquals("", PasswordEvaluator.evaluatePassword("!@Cse360*aabcdefghhhhhh"));
	}

	/**
	 * <p> Robust Test 1: Requirements Did not Meet </p>
	 * <p> String Tested: !@Cs360</p>
	 * <p> Error: Too Short</p>
	 */
	@Test
	public void RobustTest1() {
		assertNotEquals("", PasswordEvaluator.evaluatePassword("!@Cs360"));
	}

	/**
	 * <p> Robust Test 2: Requirements Did not Meet </p>
	 * <p> String Tested: !@Cse360*aabcdefghhhhhhhhh </p>
	 * <p> Error: Too long</p>
	 */
	@Test
	public void RobustTest2() {
		assertNotEquals("", PasswordEvaluator.evaluatePassword("!@Cse360*aabcdefghhhhhhhhh"));
	}

	/**
	 * <p> Robust Test 3: Requirements Did not Meet </p>
	 * <p> String Tested: !@cse360* </p>
	 * <p> Error: Missing UpperCase </p>
	 */
	@Test
	public void RobustTest3() {
		assertNotEquals("", PasswordEvaluator.evaluatePassword("!@cse360*"));
	}

	/**
	 * <p> Robust Test 4: Requirements Did not Meet </p>
	 * <p> String Tested:!@CSE360* </p>
	 * <p> Error: Missing Lowercase</p>
	 */
	@Test
	public void RobustTest4() {
		assertNotEquals("", PasswordEvaluator.evaluatePassword("!@CSE360*"));
	}

	/**
	 * <p> Robust Test 5: Requirements Did not Meet </p>
	 * <p> String Tested: !@Cseabc*</p>
	 * <p> Error: Missing Numerical Value </p>
	 */
	@Test
	public void RobustTest5() {
		assertNotEquals("", PasswordEvaluator.evaluatePassword("!@Cseabc*"));
	}

	/**
	 * <p> Robust Test 6: Requirements Did not Meet </p>
	 * <p> String Tested: atCse360a</p>
	 * <p> Error: Missing Symbols </p>
	 */
	@Test
	public void RobustTest6() {
		assertNotEquals("", PasswordEvaluator.evaluatePassword("atCse360a"));
	}

	/**
	 * <p> Robust Test 7: Requirements Did not Meet </p>
	 * <p> String Tested: !@Cse 360*</p>
	 * <p> Error: Invalid pass because a space was there in between</p>
	 */
	@Test
	public void RobustTest7() {
		assertNotEquals("", PasswordEvaluator.evaluatePassword("!@Cse 360*"));
	}

	/**
	 * <p> Robust Test 8: Requirements Did not Meet </p>
	 * <p> String Tested: "" => empty string </p>
	 * <p> Error: empty string</p>
	 */
	@Test
	public void RobustTest8() {
		assertNotEquals("", PasswordEvaluator.evaluatePassword(""));
	}
	
}
