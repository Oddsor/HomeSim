
package no.oddsor.simulator3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Odd
 */
public class Task implements ITask{

    String fulfilledNeed;
    int fulfilledAmount;
    
    Map<String, Integer> requiredItem;
    Map<String, Integer> resultingItem;
    
    public int startTime;
    public int endTime;
    
    String taskName;
    int durationMinutes;
    double remainingSeconds;
    String performedAt;
    String type;

    public Task(String taskName, String type, int durationMinutes, String performedAt) {
        this.taskName = taskName;
        this.type = type;
        this.durationMinutes = durationMinutes;
        this.remainingSeconds = (double) (durationMinutes * 60);
        this.startTime = -1;
        this.endTime = -1;
        this.performedAt = performedAt;
        fulfilledNeed = null;
        fulfilledAmount = -1;
        resultingItem = new HashMap<>();
        requiredItem = new HashMap<>();
    }
    
    public Task(String taskName, String type, int durationSeconds,
            int startTime, int endTime, String performedAt) {
        this(taskName, type, durationSeconds, performedAt);
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public void addResult(String need, int amount){
        fulfilledNeed = need;
        fulfilledAmount = amount;
    }
    
    public void addRequiredItem(String item, int amount){
        requiredItem.put(item, amount);
    }
    public void addResultingItem(String item, int amount){
        resultingItem.put(item, amount);
    }
    
    @Override
    public Collection<Appliance> getViableAppliances(Collection<Appliance> allObjects){
        Collection<Appliance> viableObjects = new ArrayList<>();
        if(performedAt != null){
                for (Appliance obj : allObjects) {
                    if (performedAt.equals(obj.type)) {
                        Iterator<String> it = requiredItem.keySet().iterator();
                        boolean fulfilled = true;
                        while(it.hasNext()){
                            if(!obj.hasItem(it.next())) fulfilled = false;
                        }
                        if(fulfilled) viableObjects.add(obj);
                    }
                }
        }else System.out.println("Task " + taskName + " cannot be performed anywhere?");
        return viableObjects;
    }
    
    public boolean taskAvailable(int day, int hour){
        if(startTime == 0 && endTime == 0) return true;
        int newEnd = (endTime - startTime) % 24;
        int newCurrent = (hour - startTime) % 24;
        
        return newCurrent <= newEnd;
    }
    
    public boolean completable(Person p, SimulationMap map){
        Iterator<String> it = requiredItem.keySet().iterator();
        while(it.hasNext()){
            String item = it.next();
            if(!p.hasItem(item, requiredItem.get(item))){
                return false;
            }
        }
        return true;
    }

    @Override
    public String fulfilledNeed() {
        return fulfilledNeed;
    }

    @Override
    public boolean available(double time) {
        if(startTime < 0 || endTime < 0) return true;
        int newEndTime = (endTime + (24 - startTime)) % 24;
        int offset = (Time.getHours(time) + (24 - startTime)) % 24;
        return offset < newEndTime;
    }

    @Override
    public boolean itemsExist(Person p, SimulationMap map) {
        
        for(String item: requiredItem.keySet()){
            if(!map.hasItem(item, requiredItem.get(item)) && !p.hasItem(item, requiredItem.get(item))) return false;
        }
        return true;
    }

    @Override
    public Map<String, Integer> getRequiredItems() {
        return requiredItem;
    }

    @Override
    public double getDurationSeconds() {
        return (double)(durationMinutes * 60);
    }

    @Override
    public boolean personHasAllItems(Person person) {
        for(String item: requiredItem.keySet()){
            if(!person.hasItem(item, requiredItem.get(item))) return false;
        }
        return true;
    }

    @Override
    public Set<String> getCreatedItems() {
        Set<String> str = new HashSet<>();
        for(String item: resultingItem.keySet()){
            str.add(item);
        }
        return str;
    }

    @Override
    public Set<String> getRequiredItemsSet() {
        return requiredItem.keySet();
    }

    @Override
    public List<String> getUsedAppliances() {
        List<String> appliances = new ArrayList<>();
        
        appliances.add(performedAt);
        
        return appliances;
    }

    @Override
    public void completeTask(Person p, SimulationMap map) {
        if(fulfilledNeed != null){
             List<Need> needs = p.getNeeds();
             for(Need need: needs){
                 if(need.name().equals(fulfilledNeed)) need.increaseValue(fulfilledAmount);
             }
        }
        if(!resultingItem.isEmpty()){
            for(String item: resultingItem.keySet()){
                for(int i = 0; i < resultingItem.get(item); i++){
                    map.addItem(new Item(item, map.getClosestNode(p.currentLocation())));
                    map.items.size();
                }
            }
        }
    }

    @Override
    public void consumeItem(Person p) {
        for(String item: requiredItem.keySet()){
            p.removeItem(item, requiredItem.get(item));
        }
    }

    @Override
    public String toString() {
        return taskName; //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void main(String[] args){
        Task t2 = new Task("Hei", "jepp", 22, null);
        System.out.println(t2.available(100));
        Task t = new Task("Hei", "jepp", 22, 9, 23, null);
        System.out.println(t.available(100));
        double tim = 80000;
        System.out.println(Time.getHours(tim));
        System.out.println(t.available(tim));
    }
}