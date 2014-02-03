
package no.oddsor.simulator3;

import java.util.Map;

/**
 *
 * @author Odd
 */
public class HouseObject implements Entity{
    
    Map<Item, Integer> inventory;
    HouseObjects type;
    Node location;
    
    public HouseObject(HouseObjects itemType, Node location){
        this.type = itemType;
        this.location = location;
    }

    @Override
    public void remove(Item item, int amount) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void add(Item item, int amount) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasItem(Item item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getAmount(Item item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
