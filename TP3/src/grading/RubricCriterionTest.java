package grading;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RubricCriterionTest {

    @Test
    public void testGetName() {
        RubricCriterion c = new RubricCriterion("Content Quality", "Some description", 30);
        assertEquals("Content Quality", c.getName());
    }

    @Test
    public void testGetDescription() {
        RubricCriterion c = new RubricCriterion("Content Quality", "Some description", 30);
        assertEquals("Some description", c.getDescription());
    }

    @Test
    public void testGetMaxPoints() {
        RubricCriterion c = new RubricCriterion("Content Quality", "Some description", 30);
        assertEquals(30, c.getMaxPoints());
    }

    @Test
    public void testZeroMaxPoints() {
        RubricCriterion c = new RubricCriterion("Empty", "No points", 0);
        assertEquals(0, c.getMaxPoints());
    }

    @Test
    public void testEmptyName() {
        RubricCriterion c = new RubricCriterion("", "", 10);
        assertEquals("", c.getName());
    }
}