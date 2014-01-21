/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator2.db_tables;

/**
 *
 * @author Odd
 */
public class Needs {
    private final double maxValue = 1.0;
    private final String name;
    private double currentValue;
    
    public Needs(String name){
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
}
