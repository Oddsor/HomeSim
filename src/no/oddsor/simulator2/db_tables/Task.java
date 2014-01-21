/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator2.db_tables;

import java.util.ArrayList;

/**
 *
 * @author Odd
 */
public interface Task {
    
    /**
     * Variables that need to be some specific value 
     * @return 
     */
    public ArrayList<Variable> requirements();
}
