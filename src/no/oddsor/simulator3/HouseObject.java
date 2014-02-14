
package no.oddsor.simulator3;

import java.util.Collection;
import no.oddsor.simulator3.enums.ObjectTypes;
import no.oddsor.simulator3.enums.Item;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Odd
 */
public class HouseObject implements Entity{
    
    Map<Item, Integer> inventory;
    ObjectTypes type;
    Node location;
    
    public HouseObject(ObjectTypes itemType, Node location){
        this.type = itemType;
        this.location = location;
        inventory = new HashMap<>();
    }

    public Node getLocation(){
        return location;
    }

    @Override
    public boolean hasItem(Item item) {
        if(inventory.containsKey(item))return true;
        return false;   
    }

    @Override
    public void removeItem(Item item, int amount) {
        inventory.put(item, getAmountOfItem(item) - amount);
    }

    @Override
    public void addItem(Item item, int amount) {
        inventory.put(item, getAmountOfItem(item) + amount);
    }

    @Override
    public int getAmountOfItem(Item item) {
        int amount = 0;
        if(inventory.get(item) != null) amount = inventory.get(item);
        return amount;
    }
}
