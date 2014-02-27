
package no.oddsor.simulator3;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Odd
 */
public class HouseObject{
    
    Map<String, Integer> inventory;
    String type;
    Node location;
    
    public HouseObject(String itemType, Node location){
        this.type = itemType;
        this.location = location;
        inventory = new HashMap<>();
    }

    public Node getLocation(){
        return location;
    }

    public boolean hasItem(String item) {
        if(inventory.containsKey(item))return true;
        return false;   
    }

    public void removeItem(String item, int amount) {
        inventory.put(item, getAmountOfItem(item) - amount);
    }

    public void addItem(String item, int amount) {
        inventory.put(item, getAmountOfItem(item) + amount);
    }

    public int getAmountOfItem(String item) {
        int amount = 0;
        if(inventory.get(item) != null) amount = inventory.get(item);
        return amount;
    }
    
    public String type(){
        return type;
    }
}
