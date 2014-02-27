
package no.oddsor.simulator3;

/**
 *
 * @author Odd
 */
class Need {
    
    private String type;
    private int deteriorationRate;
    private double value;
    
    public Need(String type, double value){
        this.type = type;
        //TODO add deteriorationrate
        this.deteriorationRate = 1;
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
        return type;
    }
    
}