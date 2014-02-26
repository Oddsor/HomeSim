
package no.oddsor.simulator3;

import no.oddsor.simulator3.enums.NeedType;

/**
 *
 * @author Odd
 */
class Need {
    
    private NeedType type;
    private int deteriorationRate;
    private double value;
    
    public Need(NeedType type, double value){
        this.type = type;
        this.deteriorationRate = type.deteriorationRate;
        this.value = value;
    }
    
    public void deteriorate(double seconds){
        value -= (deteriorationRate / (60*60*seconds));
        if(value < 0) value = 0;
    }
    
    public void increaseValue(int amount){
        value += amount;
        if(value > 100.0) value = 100.0;
    }
    
    public double getValue(){
        return value;
    }
    
    public String name(){
        return type.name();
    }
    
    public NeedType type(){
        return type;
    }
}