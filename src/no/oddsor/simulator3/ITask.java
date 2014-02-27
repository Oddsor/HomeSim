/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3;

/**
 *
 * @author Odd
 */
public interface ITask {
    public String fulfilledNeed();
    
    public boolean available(Time time);
    
    public boolean itemsExist(SimulationMap map);
    
    public Object[] getRequiredItems();
    
    public void progressTask(double seconds);
    
    public double remainingDuration();
}