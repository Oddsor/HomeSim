
package no.oddsor.simulator3;

import java.util.ArrayList;
import java.util.Collection;
import no.oddsor.simulator3.enums.NeedType;
import no.oddsor.simulator3.enums.ObjectTypes;
import no.oddsor.simulator3.enums.Item;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Odd
 */
class Task {

    NeedType fulfilledNeed;
    int fulfilledAmount;
    
    Map<Item, Integer> requiredObjectInventory;
    Map<Item, Integer> resultingObjectInventory;
    
    Map<Item, Integer> requiredPersonInventory;
    Map<Item, Integer> resultingPersonInventory;
    
    public ObjectTypes[] performedAt;
    public int[] blockedDays;
    public int startTime;
    public int endTime;
    
    String taskName;
    int durationSeconds;

    public Task(String taskName, int durationSeconds, int[] blockedDays, int startTime, int endTime, ObjectTypes... performedAtObjects) {
        this.taskName = taskName;
        this.durationSeconds = durationSeconds;
        this.blockedDays = blockedDays;
        this.startTime = startTime;
        this.endTime = endTime;
        this.performedAt = performedAtObjects;
        fulfilledNeed = null;
        fulfilledAmount = -1;
        
        requiredObjectInventory = new HashMap<>();
        resultingObjectInventory = new HashMap<>();
        requiredPersonInventory = new HashMap<>();
        resultingPersonInventory = new HashMap<>();
    }
    
    public void addResult(NeedType need, int amount){
        fulfilledNeed = need;
        fulfilledAmount = amount;
    }
    
    public void addRequiredItem(boolean person, Item item, int amount){
        if(person) requiredPersonInventory.put(item, amount);
        requiredObjectInventory.put(item, amount);
    }
    public void addResultingItem(boolean person, Item item, int amount){
        if(person) resultingPersonInventory.put(item, amount);
        else resultingObjectInventory.put(item, amount);
    }
    
    public void finishTransaction(Person p, HouseObject h){
        
    }
    
    public Collection<HouseObject> getViableObjects(Collection<HouseObject> allObjects){
        Collection<HouseObject> objects = new ArrayList<>();
        if(performedAt != null){
            for (ObjectTypes performedAt1 : performedAt) {
                for (HouseObject obj : allObjects) {
                    if (performedAt1 == obj.type) {
                        Set<Item> items = requiredObjectInventory.keySet();
                        Iterator<Item> it = items.iterator();
                        while(it.hasNext()){
                            if(!obj.hasItem(it.next())) continue;
                        
                            objects.add(obj);
                        }
                    }
                }
            }
        }else System.out.println("Task " + taskName + " cannot be performed anywhere?");
        return objects;
    }
    
    public boolean taskAvailable(int day, int hour){
        if(blockedDays != null){
            for(int i = 0; i < blockedDays.length; i++){
                if(blockedDays[i] == day) return false;
            }
        }
        if(startTime == 0 && endTime == 0) return true;
        int newEnd = (endTime - startTime) % 24;
        int newCurrent = (hour - startTime) % 24;
        
        return newCurrent <= newEnd;
    }
    
    public boolean completable(Person p, ArrayList<HouseObject> allObjects){
        Collection<HouseObject> viableObjects = getViableObjects(allObjects);
        Iterator<Item> it = requiredPersonInventory.keySet().iterator();
        while(it.hasNext()){
            Item item = it.next();
            if(!p.hasItem(item)){
                System.out.println("Person has " + item.name());
                return false;
            }
        }
        for(HouseObject obj: viableObjects){
            Iterator it2 = requiredObjectInventory.keySet().iterator();
            while(it.hasNext()){
                if(!obj.hasItem((Item) it2.next())) return false;
            }
        }
        return true;
    }
}