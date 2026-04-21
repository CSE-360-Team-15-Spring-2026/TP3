package grading;

/*******
 * <p> Title: RubricCriterion Class. </p>
 *
 * <p> Description: Represents a single grading criterion with a name, description, and
 * maximum point value. Each criterion is immutable once created and serves as a building
 * block for a grading rubric. </p>
 *
 */
public class RubricCriterion {

    /** The name of this grading criterion. */
    private final String name;

    /** A brief description of what this criterion evaluates. */
    private final String description;

    /** The maximum number of points that can be awarded for this criterion. */
    private final int maxPoints;


    /*-*******************************************************************************************

    Constructor

     */

    /**
     * Default constructor is not used
     *
     * @param name        the name of the criterion
     * @param description a brief description of what this criterion evaluates
     * @param maxPoints   the maximum points that can be awarded
     */
    public RubricCriterion(String name, String description, int maxPoints) {
        this.name = name;
        this.description = description;
        this.maxPoints = maxPoints;
    }


    /*-*******************************************************************************************

    Getters

     */

    /**
     * <p> Returns the name of this grading criterion. </p>
     *
     * @return the criterion name
     */
    public String getName()        { return name; }

    /**
     * <p> Returns the description of this grading criterion. </p>
     *
     * @return the criterion description
     */
    public String getDescription() { return description; }

    /**
     * <p> Returns the maximum points that can be awarded for this criterion. </p>
     *
     * @return the maximum point value
     */
    public int    getMaxPoints()   { return maxPoints; }
}
