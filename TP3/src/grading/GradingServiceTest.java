package grading;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the grading prototype.
 * These tests validate the review and grading workflow
 * for the instructional-team prototype.
 *
 * @author Arnav
 * @version 1.0
 */
public class GradingServiceTest {

    private GradingService gradingService;

    /**
     * Creates a fresh grading service before each test.
     */
    @BeforeEach
    public void setUp() {
        gradingService = new GradingService();
    }

    /**
     * Verifies that valid graders can mark content as reviewed.
     */
    @Test
    public void markAsReviewed_validInput_returnsTrue() {
        assertTrue(gradingService.markAsReviewed(1, "staff_arnav"));
        assertTrue(gradingService.isReviewed(1));
    }

    /**
     * Verifies that invalid content IDs are rejected.
     */
    @Test
    public void markAsReviewed_invalidContentId_returnsFalse() {
        assertFalse(gradingService.markAsReviewed(0, "staff_arnav"));
        assertFalse(gradingService.markAsReviewed(-1, "staff_arnav"));
    }

    /**
     * Verifies that unauthorized users cannot review content.
     */
    @Test
    public void markAsReviewed_invalidUser_returnsFalse() {
        assertFalse(gradingService.markAsReviewed(1, "student_arnav"));
        assertFalse(gradingService.markAsReviewed(1, ""));
        assertFalse(gradingService.markAsReviewed(1, null));
    }

    /**
     * Verifies that grading succeeds after review.
     */
    @Test
    public void assignGrade_afterReview_validInput_returnsTrue() {
        gradingService.markAsReviewed(1, "staff_arnav");
        assertTrue(gradingService.assignGrade(1, 10, "staff_arnav"));
        assertEquals(10, gradingService.getGrade(1));
    }

    /**
     * Verifies that grading fails before review.
     */
    @Test
    public void assignGrade_withoutReview_returnsFalse() {
        assertFalse(gradingService.assignGrade(1, 10, "staff_arnav"));
        assertNull(gradingService.getGrade(1));
    }

    /**
     * Verifies that invalid grades are rejected.
     */
    @Test
    public void assignGrade_invalidGrade_returnsFalse() {
        gradingService.markAsReviewed(1, "staff_arnav");
        assertFalse(gradingService.assignGrade(1, -5, "staff_arnav"));
        assertFalse(gradingService.assignGrade(1, null, "staff_arnav"));
        assertNull(gradingService.getGrade(1));
    }

    /**
     * Verifies that unauthorized users cannot assign grades.
     */
    @Test
    public void assignGrade_invalidUser_returnsFalse() {
        gradingService.markAsReviewed(1, "staff_arnav");
        assertFalse(gradingService.assignGrade(1, 10, "student_arnav"));
        assertNull(gradingService.getGrade(1));
    }

    /**
     * Verifies that an existing grade can be updated.
     */
    @Test
    public void updateGrade_existingGrade_returnsTrue() {
        gradingService.markAsReviewed(1, "staff_arnav");
        gradingService.assignGrade(1, 8, "staff_arnav");
        assertTrue(gradingService.updateGrade(1, 9, "staff_arnav"));
        assertEquals(9, gradingService.getGrade(1));
    }

    /**
     * Verifies that grade update fails if no grade exists yet.
     */
    @Test
    public void updateGrade_withoutExistingGrade_returnsFalse() {
        gradingService.markAsReviewed(1, "staff_arnav");
        assertFalse(gradingService.updateGrade(1, 9, "staff_arnav"));
        assertNull(gradingService.getGrade(1));
    }

    /**
     * Verifies that unauthorized users cannot update grades.
     */
    @Test
    public void updateGrade_invalidUser_returnsFalse() {
        gradingService.markAsReviewed(1, "staff_arnav");
        gradingService.assignGrade(1, 8, "staff_arnav");
        assertFalse(gradingService.updateGrade(1, 9, "student_arnav"));
        assertEquals(8, gradingService.getGrade(1));
    }
}