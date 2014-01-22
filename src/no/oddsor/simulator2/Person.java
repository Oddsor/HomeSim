
package no.oddsor.simulator2;

import java.awt.Point;
import no.oddsor.simulator.db_tables.Task;

/**
 *
 * @author Odd
 */
abstract class Person {
    
    private Point position;
    
    public Person(Point currentPosition){
        this.position = currentPosition;
    }
    
    /**
     * 
     * @return 
     */
    public Task getTargetTask();
    
}