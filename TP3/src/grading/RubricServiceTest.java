package grading;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class RubricServiceTest {

    // RubricService uses static state so we need to clear between tests.
    // We do this by clearing each student we touch manually via clearGrades().

    @BeforeEach
    public void setUp() {
        RubricService.clearGrades("testStudent");
        RubricService.clearGrades("studentA");
        RubricService.clearGrades("studentB");
    }

    // ── getCriteria ───────────────────────────────────────────────────────────

    @Test
    public void testGetCriteriaNotEmpty() {
        List<RubricCriterion> criteria = RubricService.getCriteria();
        assertFalse(criteria.isEmpty());
    }

    @Test
    public void testGetCriteriaIsUnmodifiable() {
        List<RubricCriterion> criteria = RubricService.getCriteria();
        assertThrows(UnsupportedOperationException.class, () -> criteria.add(
            new RubricCriterion("Extra", "desc", 5)));
    }

    @Test
    public void testCriteriaNames() {
        List<RubricCriterion> criteria = RubricService.getCriteria();
        assertEquals("Content Quality",  criteria.get(0).getName());
        assertEquals("Critical Thinking", criteria.get(1).getName());
        assertEquals("Engagement",        criteria.get(2).getName());
        assertEquals("Clarity & Writing", criteria.get(3).getName());
        assertEquals("Timeliness",        criteria.get(4).getName());
    }

    // ── getMaxTotal ───────────────────────────────────────────────────────────

    @Test
    public void testGetMaxTotal() {
        assertEquals(100, RubricService.getMaxTotal());
    }

    // ── setPoints / getPoints ─────────────────────────────────────────────────

    @Test
    public void testSetAndGetPoints() {
        RubricService.setPoints("testStudent", "Content Quality", 20);
        assertEquals(20, RubricService.getPoints("testStudent", "Content Quality"));
    }

    @Test
    public void testGetPointsDefaultsToZero() {
        assertEquals(0, RubricService.getPoints("testStudent", "Content Quality"));
    }

    @Test
    public void testPointsClampedAtMax() {
        // Content Quality max is 30 — trying to set 999 should clamp to 30
        RubricService.setPoints("testStudent", "Content Quality", 999);
        assertEquals(30, RubricService.getPoints("testStudent", "Content Quality"));
    }

    @Test
    public void testPointsClampedAtZero() {
        // Negative values should clamp to 0
        RubricService.setPoints("testStudent", "Content Quality", -5);
        assertEquals(0, RubricService.getPoints("testStudent", "Content Quality"));
    }

    @Test
    public void testSetPointsExactMax() {
        RubricService.setPoints("testStudent", "Timeliness", 10);
        assertEquals(10, RubricService.getPoints("testStudent", "Timeliness"));
    }

    // ── getTotalGrade ─────────────────────────────────────────────────────────

    @Test
    public void testGetTotalGradeNoScores() {
        assertEquals(0, RubricService.getTotalGrade("testStudent"));
    }

    @Test
    public void testGetTotalGradePartial() {
        RubricService.setPoints("testStudent", "Content Quality", 20);
        RubricService.setPoints("testStudent", "Timeliness", 5);
        assertEquals(25, RubricService.getTotalGrade("testStudent"));
    }

    @Test
    public void testGetTotalGradeFull() {
        for (RubricCriterion c : RubricService.getCriteria()) {
            RubricService.setPoints("testStudent", c.getName(), c.getMaxPoints());
        }
        assertEquals(100, RubricService.getTotalGrade("testStudent"));
    }

    // ── hasGrade ──────────────────────────────────────────────────────────────

    @Test
    public void testHasGradeFalseWhenEmpty() {
        assertFalse(RubricService.hasGrade("testStudent"));
    }

    @Test
    public void testHasGradeTrueAfterSet() {
        RubricService.setPoints("testStudent", "Engagement", 10);
        assertTrue(RubricService.hasGrade("testStudent"));
    }

    // ── clearGrades ───────────────────────────────────────────────────────────

    @Test
    public void testClearGradesRemovesScores() {
        RubricService.setPoints("testStudent", "Content Quality", 25);
        RubricService.clearGrades("testStudent");
        assertEquals(0, RubricService.getTotalGrade("testStudent"));
        assertFalse(RubricService.hasGrade("testStudent"));
    }

    // ── buildStudentLabel ─────────────────────────────────────────────────────

    @Test
    public void testBuildStudentLabelNotGraded() {
        String label = RubricService.buildStudentLabel("testStudent");
        assertTrue(label.contains("not graded"));
    }

    @Test
    public void testBuildStudentLabelAfterGrading() {
        RubricService.setPoints("testStudent", "Content Quality", 20);
        String label = RubricService.buildStudentLabel("testStudent");
        assertTrue(label.contains("20"));
        assertTrue(label.contains("100"));
    }

    // ── computeRunningTotal ───────────────────────────────────────────────────

    @Test
    public void testComputeRunningTotalZero() {
        assertEquals(0, RubricService.computeRunningTotal(new int[]{0, 0, 0, 0, 0}));
    }

    @Test
    public void testComputeRunningTotalSum() {
        assertEquals(55, RubricService.computeRunningTotal(new int[]{10, 15, 20, 5, 5}));
    }

    @Test
    public void testComputeRunningTotalFull() {
        assertEquals(100, RubricService.computeRunningTotal(new int[]{30, 25, 20, 15, 10}));
    }

    // ── isolation between students ────────────────────────────────────────────

    @Test
    public void testStudentsAreIsolated() {
        RubricService.setPoints("studentA", "Content Quality", 30);
        RubricService.setPoints("studentB", "Content Quality", 10);
        assertEquals(30, RubricService.getPoints("studentA", "Content Quality"));
        assertEquals(10, RubricService.getPoints("studentB", "Content Quality"));
    }
}