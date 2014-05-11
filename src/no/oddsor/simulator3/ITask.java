/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Odd
 */
public interface ITask {
    String fulfilledNeed();
    
    boolean available(double time);
    
    boolean itemsExist(Person p, SimulationMap map);
    
    boolean personMeetsRequirements(Person person);
    
    Collection<Appliance> getViableAppliances(Collection<Appliance> allAppliances);
    
    Map<String, Integer> getRequiredItems();
    
    double getDurationSeconds();
    
    Set<String> getCreatedItems();
    
    Set<String> getRequiredItemsSet();
    
    Collection<String> getUsedAppliances();
    
    void completeTask(Person p, SimulationMap map);
    
    void consumeItem(Person p);
    
    String name();

    Set<String> getPoses();
    
    Set<String> getNeg();
    Set<String> getPos();
    Set<String> getPrecond();

    String label();
    String getType();

    boolean itemExists(Person person, SimulationMap map);

    void recentlyCompleted();

    void passTime(double d);
}