/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import no.oddsor.simulator2.db_tables.Task;
import no.oddsor.simulator2.db_tables.Variable;
import no.oddsor.simulator2.db_tables.Node;

/**
 *
 * @author Odd
 */
abstract class ISimulation {
    /**
     * @param person The person whose location we're moving from
     * @param maxMovement Maximum movement speed in pixels
     * @param target Node we're moving towards
     * @return 
     */
    abstract Point getNextLocation(Person person, Node target, int maxMovement);
    
    /**
     * 
     * @param person The person whose utility weights we need
     * @param tasks Tasks we can choose from
     * @return 
     */
    abstract Task getNextTask(Person person, ArrayList<Task> tasks);
    
    /**
     * By doing things like starting a washing machine, we will eventually have clean
     * laundry, which the task "Get laundry" requires to become an option. Until
     * laundry exists we can filter out that task from available pool of tasks.
     * @param upcomingVariables
     * @return 
     */
    abstract ArrayList<Task> filterTasks(ArrayList<Task> tasks, 
            HashMap<Variable, Integer> upcomingVariables);
}
