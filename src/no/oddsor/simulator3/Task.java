
package no.oddsor.simulator3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
    String label;
    private final Set<String> poseSet;
    private final Set<String> precond;
    private final Set<String> pos;
    private final Set<String> neg;
    private double cooldownMax;
    private double cooldown;

    public Task(String taskName, String type, int durationMinutes, String performedAt, String label) {
        this.taskName = taskName;
        this.label = label;
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
        this.poseSet = new HashSet<>();
        this.precond = new HashSet<>();
        this.pos = new HashSet<>();
        this.neg = new HashSet<>();
        this.cooldownMax = 0.0;
        this.cooldown = 0.0;
    }
    
    public Task(String taskName, String type, int durationMinutes, String performedAt, String label, int cooldown) {
        this(taskName, type, durationMinutes, performedAt, label);
        this.setCooldown(cooldown*60.0);
    }

    @Override
    public Set<String> getPrecond() {
        return precond;
    }

    @Override
    public Set<String> getPos() {
        return pos;
    }

    @Override
    public Set<String> getNeg() {
        return neg;
    }
    
    public Task(String taskName, String type, int durationSeconds,
            int startTime, int endTime, String performedAt, String label) {
        this(taskName, type, durationSeconds, performedAt, label);
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
        if(startTime < 0 || endTime < 0 && cooldown == 0.0) return true;
        int newEndTime = (endTime + (24 - startTime)) % 24;
        int offset = (Time.getHours(time) + (24 - startTime)) % 24;
        return offset < newEndTime && cooldown == 0.0;
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
    public boolean personMeetsRequirements(Person person) {
        return personHasItems(person) && personHasStates(person);
    }
    
    private boolean personHasStates(Person person){
        Set<String> personStates = person.getState();
        return personStates.containsAll(precond);
    }
    
    private boolean personHasItems(Person person){
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
    public Collection<String> getUsedAppliances() {
        Collection<String> appliances = new ArrayList<>();
        
        appliances.add(performedAt);
        
        return appliances;
    }

    @Override
    public void completeTask(Person p, SimulationMap map) {
        for(String state: pos){
            p.addState("+"+state);
        }
        for(String state: neg){
            p.addState("-"+state);
        }
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
        Task t2 = new Task("Hei", "jepp", 22, null, "hei");
        System.out.println(t2.available(100));
        Task t = new Task("Hei", "jepp", 22, 9, 23, null, "hei");
        System.out.println(t.available(100));
        double tim = 80000;
        System.out.println(Time.getHours(tim));
        System.out.println(t.available(tim));
    }

    @Override
    public String name() {
        return taskName;
    }

    public void addPose(String poseString) {
        poseSet.add(poseString);
    }

    @Override
    public Set<String> getPoses() {
        return poseSet;
    }

    public void addNeededState(String poseString) {
        precond.add(poseString);
    }

    public void addPlusState(String substring) {
        pos.add(substring);
    }

    public void addMinusState(String substring) {
        neg.add(substring);
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public boolean itemExists(Person person, SimulationMap map) {
        for(String item: requiredItem.keySet()){
            if(map.hasItem(item, requiredItem.get(item)) && !person.hasItem(item, requiredItem.get(item))) return true;
        }
        return false;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void recentlyCompleted() {
        this.cooldown = cooldownMax;
    }

    public void setCooldown(double d) {
        this.cooldownMax = d;
    }

    @Override
    public void passTime(double d) {
        this.cooldown -= d;
        if(cooldown < 0.0) cooldown = 0.0;
    }
}