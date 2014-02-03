
package no.oddsor.simulator2.db_tables;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Odd
 */
public class Needs {
    private final double maxValue = 1.0;
    private final String name;
    private double currentValue;
    
    public static Map<Integer, Needs> getNeeds(){
        Map<Integer, Needs> needs = new HashMap<>();
        
        needs.put(0, new Needs(0, "Hunger"));
        needs.put(1, new Needs(1, "Energy"));
        needs.put(2, new Needs(2, "Bladder"));
        needs.put(3, new Needs(3, "Hygiene"));
        needs.put(4, new Needs(4, "Fun"));
        
        return needs;
    }
    
    public Needs(int id, String name){
        this.name = name;
        this.currentValue = maxValue;
    }
    
    public String getName() {
        return name;
    }
    
    public void increaseNeed(double value){
        currentValue += value;
        if(currentValue > maxValue) currentValue = maxValue;
        if(currentValue < 0.0) currentValue = 0.0;
    }
    
    public double value(){
        return currentValue;
    }

}
