
package no.oddsor.simulator3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Odd
 */
class Task {

    Map<String, Integer> requirements;
    Map<String, Integer> results;
    
    Set<Integer> requiredInventory;
    Set<Integer> resultingInventory;
    
    Map<Need, Double> resultingNeed;
    
    String taskName;
    int durationSeconds;

    public Task(String taskName, int durationSeconds) {
        this.taskName = taskName;
        this.durationSeconds = durationSeconds;
        
        requirements = new HashMap<>();
        results = new HashMap<>();
        
        requiredInventory = new HashSet<>();
        resultingInventory = new HashSet<>();
    }
    
    public void addRequirement(String requirement, int amount){
        requirements.put(requirement, amount);
    }
    
    public void addRequiredItem(int item){
        requiredInventory.add(item);
    }
    public void addResultingItem(int item){
        resultingInventory.add(item);
    }
    
    Node getNode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
