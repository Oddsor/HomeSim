
package no.oddsor.simulator2.db_tables;

/**
 *
 * @author Odd
 */
public interface Variable{
    /**
     * Increases the variable by amount x. Value will be always be between 0 and 1
     * @param value 
     */
    public void increase(double value);

    public Boolean toBoolean();
}