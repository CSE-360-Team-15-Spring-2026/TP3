package entityClasses;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * <p> Title: adminRequestsTest </p>
 *
 * <p> Description: A class under the entityClasses package that tests
 * the functionality of the adminRequests.java file. </p>
 * <p> Contains tests to see if user, body, recievingAdmin, are properly stored. </p>
 */
class adminRequestsTest {
	
    /**
     * <p> Normal Test 1 </p>
     * <p> Requirement Met </p>
     */
    @Test
    public void NormalTest1() {
        adminRequests request = new adminRequests("Hector", "I need a invitation for a student", "Nayef");
        assertEquals("Hector", request.getRequestSubmiter());
        assertEquals("I need a invitation for a student", request.getBody());
        assertEquals("Nayef", request.getRecievingAdmin());
        assertEquals(false, request.getCompleted());
    }
    
    /**
     * <p> Normal Test 2 </p>
     * <p> Requirement Met</p>
     */
    @Test
    public void NormalTest2() {
        adminRequests request = new adminRequests();
        assertEquals("", request.getBody());
        assertEquals("", request.getRequestSubmiter());
        assertEquals("", request.getRecievingAdmin());
        assertEquals(false, request.getCompleted());
    }
    
    /**
     * <p> Normal Test 3 </p>
     * <p> Requirement Met</p>
     */
    @Test
    public void NormalTest3() {
        adminRequests request = new adminRequests("Jack", "A student needs to be removed", "Arnav");
        request.completeRequests();
        assertEquals(true, request.getCompleted());
    }
    
    /**
     * <p> Normal Test 4 </p>
     * <p> Requirement Met </p>
     */
    @Test
    public void NormalTest4() {
        adminRequests request = new adminRequests();
        request.setRequestID(5);
        assertEquals(5, request.getRequestID());
    }
    
    /**
     * <p> Normal Test 5 </p>
     * <p> Requirement Met </p>
     */
    @Test
    public void NormalTest5() {
        adminRequests request = new adminRequests();
        request.setFirstRequestID(2);
        assertEquals(2, request.getFirstRequestID());
    }
    
    /**
     * <p> Normal Test 6 </p>
     * <p> Requirement Met </p>
     */
    @Test
    public void NormalTest6() {
        adminRequests request = new adminRequests();
        assertNotNull(request.getTimeStamp());
    }
    
    /**
     * <p> Robust Test 1 - Requirements Not Met </p>
     * <p> Error: Empty body </p>
     */
    @Test
    public void RobustTest1() {
        adminRequests request = new adminRequests("Fauzan", "", "Agastaya");
        assertEquals(false, request.validateBody());
    }
    
    /**
     * <p> Robust Test 2 - Requirements Not Met </p>
     * <p> Error: Null body </p>
     */
    @Test
    public void RobustTest2() {
        adminRequests request = new adminRequests("Jack", null, "Hector");
        assertEquals(false, request.validateBody());
    }
    
    /**
     * <p> Robust Test 3 - Requirements Not Met </p>
     * <p> Error: Null submitter </p>
     */
    @Test
    public void RobustTest3() {
        adminRequests req = new adminRequests(null, "We need to change roles for a student", "Jack");
        
        assertNull(req.getRequestSubmiter());
    }
    
    /**
     * <p> Robust Test 4 - Requirements Not Met </p>
     * <p> Error: Null admin </p>
     */
    @Test
    public void RobustTest4() {
        adminRequests req = new adminRequests("Arnav", "The student sammy needs to be removed", null);
        
        assertNull(req.getRecievingAdmin());
    }

}
