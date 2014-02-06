
package no.oddsor.simulator3;

import no.oddsor.simulator3.enums.Item;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
import no.oddsor.simulator3.enums.NeedType;

/**
 *
 * @author Odd
 */
class Person implements Entity{

    private Point currentLocation;
    private ArrayList<Node> route;
    private ArrayList<Need> needs;
    private Task currentTask;
    private double taskCount;
    public final String name;
    
    private HashMap<Item, Integer> inventory;
    public Image avatarImg;
    
    public Person(String name, String avatarImage, Point currentLocation){
        this.name = name;
        this.currentLocation = currentLocation;
        this.avatarImg = new ImageIcon(getClass().getResource(avatarImage)).getImage();
        needs = new ArrayList<>();
        NeedType[] needlist = NeedType.values();
        for(int i = 0; i < needlist.length; i++){
            needs.add(new Need(needlist[i], 100.0));
        }
    }
    
    Point currentLocation() {
        return currentLocation;
    }

    ArrayList<Node> getRoute() {
        if(route == null || route.isEmpty()) return null;
        return route;
    }

    void setLocation(Point moveToPoint) {
        currentLocation = moveToPoint;
        if((route != null || !route.isEmpty()) && 
                currentLocation.distance(route.get(0).getLocation()) == 0){
            route.remove(0);
        }
    }
    
    public ArrayList<Need> getSortedNeeds(){
        return needs;
    }

    void setTask(Task nextTask, SimulationMap map) {
        if(nextTask != null){
            currentTask = nextTask;
            taskCount = currentTask.durationSeconds;
            System.out.println(name + " decided to do " + currentTask.taskName);
            route = map.getRouteToObject(currentTask.getViableObjects(map.objects), currentLocation);
        }
    }
    
    public Task getTask(){
        return currentTask;
    }

    void passTime(double seconds) {
        if(getRoute() == null){
            taskCount -= seconds;
            if(taskCount <= 0.0){
                //TODO transactions
                currentTask = null;
            }
        }
        for(Need need: needs){
            need.deteriorate(seconds);
        }
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
