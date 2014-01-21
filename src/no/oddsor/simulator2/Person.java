/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator2;

import no.oddsor.simulator.db_tables.Task;

/**
 *
 * @author Odd
 */
public interface Person {
    
    /**
     * 
     * @return 
     */
    public Task getTargetTask();
    
}
