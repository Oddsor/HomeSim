/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Odd
 */
public interface ITask {
    public String fulfilledNeed();
    
    public boolean available(double time);
    
    public boolean itemsExist(SimulationMap map);
    
    public boolean personHasAllItems(Person person);
    
    public Collection<HouseObject> getViableAppliances(Collection<HouseObject> allAppliances);
    
    public Map<String, Integer> getRequiredItems();
    
    public double getDurationSeconds();
}