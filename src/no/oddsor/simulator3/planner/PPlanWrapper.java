/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3.planner;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.oddsor.simulator3.ITask;
import no.oddsor.simulator3.Item;
import no.oddsor.simulator3.Person;
import no.oddsor.simulator3.SimulationMap;
import plplan.javaapi.EnumAlgorithm;
import plplan.javaapi.PLPlan;

/**
 *
 * @author Odd
 */
public class PPlanWrapper {
    
    
    public static <Task extends ITask> Deque<Task> getPlan(SimulationMap m, Person p, Collection<Task> tasks, Task goalTask) throws Exception{
        PLPlan plan = new PLPlan();
        plan.setAlgorithm(EnumAlgorithm.GRAPHPLAN);
        Collection<String> requiredFacts = getFacts(goalTask);
        for(String f: requiredFacts){
            plan.addGoalFact(f);
        }
        Collection<String> currentFacts = getState(m, p);
        for(String s: currentFacts){
            plan.addFact(s);
        }
        for(Task t: tasks){
            addTask(t, plan);
        }
        List planList = plan.findPlan();
        Deque<Task> taskPlan = new ArrayDeque<>();
        for(Object ob: planList){
            List pl = null;
            if(ob instanceof ArrayList){
                pl = (List) ob;
                for(Object obs: pl){
                    for(Task t: tasks){
                        if(obs.toString().equals(t.name())) taskPlan.add(t);
                    }
                }
            }
        }
        
        return taskPlan;
    }
    
    private static Collection<String> getFacts(ITask goalTask){
        Collection<String> reqFacts = new ArrayList<>();
        Map<String, Integer> requiredItems = goalTask.getRequiredItems();
        for(String fact: requiredItems.keySet()){
            reqFacts.add(fact);
        }
        reqFacts.addAll(goalTask.getPrecond());
        return reqFacts;
    }
    
    private static Collection<String> getState(SimulationMap m, Person p){
        Collection<Item> items = m.items;
        Collection<String> state = new ArrayList<>();
        Map<String, Integer> inventory = p.getInventory();
        for(Item item: items){
            state.add(item.name);
        }
        for(String s: inventory.keySet()){
            state.add(s);
        }
        state.addAll(p.getState());
        return state;
    }

    private static void addTask(ITask t, PLPlan plan) {
        List<String> precond = new ArrayList<>();
        List<String> pos = new ArrayList<>();
        List<String> neg = new ArrayList<>();
        Map<String, Integer> k = t.getRequiredItems();
        precond.addAll(t.getPrecond());
        pos.addAll(t.getPos());
        neg.addAll(t.getNeg());
        
        for(String s: k.keySet()){
            precond.add(s);
            neg.add(s);
        }
        Set<String> created = t.getCreatedItems();
        for(String s: created){
            pos.add(s);
        }
        plan.addOperator(t.name(), precond, neg, pos);
        //System.out.println(t.name() + ", P" + precond.toString() + ", -" + neg.toString() + ", +" + pos.toString());
    }
    
    public static void main(String[] args){
        String t = "2014-01-01 00:00:16.333333 KitchenCamera13 KitchenCamera13 ON Eat_food";
        String q = "2014-01-01 00:00:16.666667 KitchenCamera13 KitchenCamera13 +Leaning Eat_food";
        
        System.out.println(!t.contains("ON") && !t.contains("OFF"));
        System.out.println(!q.contains("ON") && !q.contains("OFF"));
    }
}
