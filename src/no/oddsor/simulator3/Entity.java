
package no.oddsor.simulator3;

/**
 *
 * @author Odd
 */
public interface Entity {
    public void remove(Item item, int amount);
    
    public void add(Item item, int amount);
    
    public boolean hasItem(Item item);
    
    public int getAmount(Item item);
}
