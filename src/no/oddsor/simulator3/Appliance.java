
package no.oddsor.simulator3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import no.oddsor.simulator3.tables.Node;


public class Appliance{
    
    Map<String, Integer> inventory;
    public String type;
    Node location;
    private Set<String> poses;
    
    public Appliance(String itemType, Node location){
        this.type = itemType;
        this.location = location;
        inventory = new HashMap<>();
        this.poses = new HashSet<>();
    }
    public Appliance(String itemType, Node location, Set<String> poses){
        this(itemType, location);
        this.poses = poses;
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
    public String getName(){
        return type+getLocation().id;
    }

    public Set<String> getPoses() {
        return poses;
    }
    
    public void setPoses(String poseString){
        this.poses = new HashSet<String>(Arrays.asList(poseString.split(" ")));
    }
}
