
package no.oddsor.simulator3.enums;

/**
 *
 * @author Odd
 */
public enum NeedType {
    HUNGER (10), ENERGY(5), FUN(20), TOILET(20), HYGIENE(5);
    
    public int deteriorationRate;
    private NeedType(int deteriorationRate){
        this.deteriorationRate = deteriorationRate;
    }
}
