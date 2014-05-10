
package no.oddsor.simulator3;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import no.oddsor.simulator3.tables.Node;


public class Person{

    private Point currentLocation;
    private Deque<Node> route;
    private final List<Need> needs;
    private ITask currentTask;
    public final String name;
    private double fetchTime;
    private double remainingTaskDuration;
    
    public String targetItem;
    
    Set<String> state;
    
    private final HashMap<String, Integer> inventory;

    public HashMap<String, Integer> getInventory() {
        return inventory;
    }
    public Image avatarImg;
    private ITask goalTask;
    private Appliance usingAppliance;

    public Appliance getUsingAppliance() {
        return usingAppliance;
    }
    
    public Person(String name, String avatarImage, Point currentLocation, List<Need> needs){
        this.name = name;
        this.currentLocation = currentLocation;
        this.avatarImg = new ImageIcon(getClass().getResource(avatarImage)).getImage();
        if(needs != null) this.needs = new ArrayList<>(needs);
        else this.needs = null;
        inventory = new HashMap<>();
        state = new HashSet<>();
    }
    
    public void addNeed(Need need){
        needs.add(need);
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

    Deque<Node> getRoute() {
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
    
    private void setAppliance(Appliance app){
        this.usingAppliance = app;
    }
    
    public List<Need> getNeeds(){
        return needs;
    }
    
    private void setCurrentTask(ITask task){
        System.out.println("Task set");
        this.currentTask = task;
        if(task != null) this.remainingTaskDuration = task.getDurationSeconds();
    }
    
    public void setCurrentTask(ITask task, Appliance appliance){
        this.setCurrentTask(task);
        this.setAppliance(appliance);
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
        System.out.println("Goal set");
        this.goalTask = task;
    }
    
    public void setRoute(Deque<Node> route){
        this.route = route;
    }

    public ITask getCurrentTask(){
        return currentTask;
    }
    
    public void setTargetItem(String itemName){
        System.out.println("Item set");
        this.targetItem = itemName;
        fetchTime = 2.0;
    }
    
    public String getTargetItem(){
        return targetItem;
    }

    public void progressFetch(double seconds){
        this.fetchTime -= seconds;
    }
    
    void passTime(double seconds) {
        for(Need need: needs){
            need.deteriorate(seconds);
        }
    }
    
    public double getFetchTime(){
        return fetchTime;
    }
    
    public void removeItem(String itemName, int amount){
        inventory.put(itemName, inventory.get(itemName) - amount);
        if(inventory.get(itemName) <= 0) inventory.remove(itemName);
    }
    
    public Set<String> getPoseData(){
        Set<String> poses = new HashSet<>();
        if(currentTask != null) poses.addAll(currentTask.getPoses());
        //poses.addAll(usingAppliance.getPoses());
        return poses;
    }

    boolean isMoving() {
        return (this.currentTask != null || this.targetItem != null) && (route != null && route.size() > 0);
    }
    
    void addState(String stateString){
        if(stateString.charAt(0) == '+') state.add(stateString.substring(1));
        else state.remove(stateString.substring(1));
    }
    
    public Set<String> getState(){
        return state;
    }
}