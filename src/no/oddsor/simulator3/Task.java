
package no.oddsor.simulator3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    List<HouseObject> performedAt;
    String type;

    public Task(String taskName, String type, int durationMinutes, List<HouseObject> performedAt) {
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
            int startTime, int endTime, List<HouseObject> performedAt) {
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
    public Collection<HouseObject> getViableAppliances(Collection<HouseObject> allObjects){
        Collection<HouseObject> viableObjects = new ArrayList<>();
        if(performedAt != null){
            for (HouseObject appliance : performedAt) {
                for (HouseObject obj : allObjects) {
                    if (appliance.type.equals(obj.type)) {
                        Iterator<String> it = requiredItem.keySet().iterator();
                        boolean fulfilled = true;
                        while(it.hasNext()){
                            if(!obj.hasItem(it.next())) fulfilled = false;
                        }
                        if(fulfilled) viableObjects.add(obj);
                    }
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
        int newEndTime = (endTime - startTime) % 24;
        return (Time.getHours(time) % 24) > newEndTime;
    }

    @Override
    public boolean itemsExist(SimulationMap map) {
        
        for(String item: requiredItem.keySet()){
            if(!map.hasItem(item, requiredItem.get(item))) return false;
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
}