
package no.oddsor.simulator3;

/**
 *
 * @author Odd
 */
public class Need implements Comparable<Need>{
    
    private String type;
    private double deteriorationRate;
    private double value;
    
    public Need(String type, double deteriorationRate){
        this.type = type;
        //TODO add deteriorationrate
        this.deteriorationRate = deteriorationRate;
        this.value = 100;
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

    @Override
    public int compareTo(Need t) {
        return (int) (this.value - t.value);
    }
    
}