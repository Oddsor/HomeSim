
package no.oddsor.simulator3;

import no.oddsor.simulator3.enums.Item;

/**
 *
 * @author Odd
 */
public interface Entity {
    public void removeItem(Item item, int amount);
    
    public void addItem(Item item, int amount);
    
    public boolean hasItem(Item item);
    
    public int getAmountOfItem(Item item);
}
