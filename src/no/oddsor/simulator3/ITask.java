/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Odd
 */
public interface ITask {
    public String fulfilledNeed();
    
    public boolean available(double time);
    
    public boolean itemsExist(Person p, SimulationMap map);
    
    public boolean personHasAllItems(Person person);
    
    public Collection<Appliance> getViableAppliances(Collection<Appliance> allAppliances);
    
    public Map<String, Integer> getRequiredItems();
    
    public double getDurationSeconds();
    
    public Set<String> getCreatedItems();
    
    public Set<String> getRequiredItemsSet();
    
    public List<String> getUsedAppliances();
    
    public void completeTask(Person p, SimulationMap map);
    
    public void consumeItem(Person p);
    
    public String name();
}