/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator;

import java.util.ArrayList;
import no.oddsor.simulator.db_tables.*;


/**
 *
 * @author Odd
 */
public interface IPerson {
    public java.awt.Point getPosition();
    
    public java.awt.Point nextPosition();
    
    public Task getGoalTask();
    
    /**
     * Find a path through all prerequisite tasks
     * @return 
     */
    public ArrayList<Path> findGoal();
    
    public void setGoalTask();
    
    public Path getCurrentPath();
}
