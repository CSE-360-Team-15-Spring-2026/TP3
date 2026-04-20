package grading;

/**
 * Represents a single grading criterion with a name, description, and max points.
 */
public class RubricCriterion {
    private final String name;
    private final String description;
    private final int maxPoints;

    public RubricCriterion(String name, String description, int maxPoints) {
        this.name = name;
        this.description = description;
        this.maxPoints = maxPoints;
    }

    public String getName()        { return name; }
    public String getDescription() { return description; }
    public int    getMaxPoints()   { return maxPoints; }
}