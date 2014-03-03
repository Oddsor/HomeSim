
package no.oddsor.simulator3;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import javax.swing.ImageIcon;

/**
 *
 * @author Odd
 */
public class Person{

    private Point currentLocation;
    private Deque<Node> route;
    private final List<Need> needs;
    private ITask currentTask;
    private HouseObject usingObject;
    private double taskCount;
    public final String name;
    private double fetchTime;
    private double remainingTaskDuration;
    
    public String targetItem;
    
    private final HashMap<String, Integer> inventory;
    public Image avatarImg;
    private ITask goalTask;
    
    public Person(String name, String avatarImage, Point currentLocation){
        this.name = name;
        this.currentLocation = currentLocation;
        this.avatarImg = new ImageIcon(getClass().getResource(avatarImage)).getImage();
        needs = new ArrayList<>();
        inventory = new HashMap<>();
    }
    
    Point currentLocation() {
        return currentLocation;
    }
    
    public void addItem(Item item){
        inventory.put(item.name, (inventory.get(item.name) != null ? 
                inventory.get(item.name) : 0) + 1);
    }
    
    public boolean hasItem(String item, int amount){
        if(inventory.containsKey(item)){
            if(inventory.get(item) >= amount) return true;
        }
        return false;
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
    
    public List<Need> getNeeds(){
        return needs;
    }
    
    public void setCurrentTask(ITask task){
        this.currentTask = task;
        this.remainingTaskDuration = task.getDurationSeconds();
    }
    
    public double remainingDuration(){
        return remainingTaskDuration;
    }
    
    public void progressTask(double seconds){
        remainingTaskDuration -= seconds;
    }
    
    public ITask getGoalTask(){
        return goalTask;
    }
    public void setGoalTask(ITask task){
        this.goalTask = task;
    }
    
    public void setRoute(Deque<Node> route){
        this.route = route;
    }

/*    void setTask(Task nextTask, SimulationMap map) {
        if(nextTask != null){
            currentTask = nextTask;
            taskCount = currentTask.durationSeconds;
            System.out.println(name + " decided to do " + currentTask.taskName);
            Collection<Node> nodes = new ArrayList<>();
            Collection<Person> people = map.getPeople();
            for(HouseObject object: currentTask.getViableObjects(map.objects)){
                boolean inUse = false;
                for(Person p: people){
                    if(p.usingObject != null && p.usingObject.equals(object)) inUse = true;
                }
                if(!inUse)nodes.add(object.getLocation());
            }
            try {
                route = AStarMulti.getRoute(nodes, map.getClosestNode(currentLocation));
                if(!route.isEmpty()){
                    for(HouseObject object: currentTask.getViableObjects(map.objects)){
                        if(object.getLocation().equals(route.getLast())) usingObject = object;
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }*/
    
    public ITask getCurrentTask(){
        return currentTask;
    }
    
    public void setTargetItem(String itemName){
        this.targetItem = itemName;
        fetchTime = 2.0;
    }
    
    public String getTargetItem(){
        return targetItem;
    }

    void passTime(double seconds) {
        for(Need need: needs){
            need.deteriorate(seconds);
        }
        fetchTime -= seconds;
    }
    
    public double getFetchTime(){
        return fetchTime;
    }
}
