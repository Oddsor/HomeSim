
package no.oddsor.simulator3;

import no.oddsor.simulator3.enums.Item;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import net.oddsor.AStarMulti.AStarMulti;
import net.oddsor.AStarMulti.AStarNode;
import no.oddsor.simulator3.enums.NeedType;

/**
 *
 * @author Odd
 */
class Person implements Entity{

    private Point currentLocation;
    private Queue<Node> route;
    private List<Need> needs;
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
        for (NeedType needlist1 : needlist) {
            needs.add(new Need(needlist1, 100.0));
        }
    }
    
    Point currentLocation() {
        return currentLocation;
    }

    Queue<Node> getRoute() {
        if(route == null || route.isEmpty()) return null;
        return route;
    }

    void setLocation(Point moveToPoint) {
        currentLocation = moveToPoint;
        if((route != null || !route.isEmpty()) && 
                currentLocation.distance(route.peek().getLocation()) == 0){
            route.remove();
        }
    }
    
    public List<Need> getSortedNeeds(){
        return needs;
    }

    void setTask(Task nextTask, SimulationMap map) {
        if(nextTask != null){
            currentTask = nextTask;
            taskCount = currentTask.durationSeconds;
            System.out.println(name + " decided to do " + currentTask.taskName);
            Collection<Node> nodes = new ArrayList<>();
            for(HouseObject object: currentTask.getViableObjects(map.objects)){
                nodes.add(object.getLocation());
            }
            try {
                route = AStarMulti.getRoute(nodes, map.getClosestNode(currentLocation));
            } catch (Exception ex) {
                Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        return inventory.containsKey(item);   
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
