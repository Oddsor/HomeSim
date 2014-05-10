
package no.oddsor.simulator3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.oddsor.AStarMulti.AStarMulti;
import no.oddsor.simulator3.json.JSON;
import no.oddsor.simulator3.planner.PPlanWrapper;
import no.oddsor.simulator3.tables.Node;
import plplan.javaapi.EnumAlgorithm;
import plplan.javaapi.PLPlan;

/**
 *
 * @author Odd
 */
public class TaskManager {
    
    private final Collection<ITask> tasks;
    
    public TaskManager(JSON j){
        tasks = j.getTasks();
    }
    
    public static void main(String[] args){
        JSON j = null;
        try {
            j = new JSON("tasks.json");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Collection<ITask> tsk = j.getTasks();
        System.out.println(tsk.size());
        Person p = new Person("Oddser", "oddsurcut.png", null, null);
        SimulationMap mss = new SimulationMap(null, 0, 0, null, 0, null);
        ITask goal = null;
        for(ITask task: tsk){
            if(task.name().equals("Eat food")) goal = task;
        }
        try {
            PPlanWrapper.getPlan(mss, p, tsk, goal);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void findTask(Person person, SimulationMap map, double time)
    {
        Collection<ITask> availableTasks = new ArrayList<>(filterAvailable(tasks, time));
        if(person.getGoalTask() != null && !person.getGoalTask().available(time)) person.setGoalTask(null);
        if(person.getGoalTask() != null)
        {
            if(person.getGoalTask().personMeetsRequirements(person))
            {
                setTaskForPerson(person, person.getGoalTask(), map);
                person.setGoalTask(null);
            }else if(person.getGoalTask().getRequiredItems().size() > 0 && person.getGoalTask().itemsExist(person, map))
            {
                    moveForItems(person, person.getGoalTask(), map);
            }else{
                try {
                    System.out.println("Finding plan for " + person.getGoalTask().name());
                    Deque<ITask> tusks = PPlanWrapper.getPlan(map, person, availableTasks, person.getGoalTask());
                    tusks.toString();
                    setTaskForPerson(person, tusks.getFirst(), map);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{
            //IF NO GOALTASK, WHAT THEN? CREATE A GOAL
            System.out.println("Finding a new goal task");
            List<Need> needs = new ArrayList<>(person.getNeeds());
            while(!needs.isEmpty()){
                Need lowest = getLowestNeed(needs);
                System.out.println(lowest.name() +  " is lowest need");
                ITask taskForNeed = taskForNeed(lowest, availableTasks, time, person, map);
                if(taskForNeed != null){
                    person.setGoalTask(taskForNeed);
                    break;
                }else{
                    needs.remove(lowest);
                }
            }
        }
    }
    
    public void findTaskLoop(ITask currentTask, Person person, SimulationMap map){
        if(currentTask.personMeetsRequirements(person)){
            setTaskForPerson(person, currentTask, map);
        }
        else if(currentTask.itemsExist(person, map)){
            System.out.println("Yay, items found for " + currentTask.toString());
            moveForItems(person, currentTask, map);
        }
        else{
            System.out.println(currentTask.getRequiredItemsSet().toString());
            Collection<ITask> prereqTasks = tasksForGoal(currentTask, person, map);
            System.out.println(prereqTasks.toString());
            for(ITask task: prereqTasks){
                findTaskLoop(task, person, map);
                if(person.targetItem != null || person.getCurrentTask() != null) break;
            }
        }
    }
    
    public Collection<ITask> tasksForGoal(ITask goal, Person p, SimulationMap map){
        System.out.println("Finding tasks for " + goal.toString() + " (reqs: " + goal.getRequiredItemsSet().toString() + ")");
        Collection<ITask> preTasks = new HashSet<>();
        for(ITask task: tasks){
            for(String str: task.getCreatedItems()){
                if(goal.getRequiredItemsSet().contains(str)){
                    System.out.println("Adding " + task.toString());
                    System.out.println(task.itemsExist(p, map));
                    if(!task.itemsExist(p, map)) preTasks.addAll(tasksForGoal(task, p, map));
                    else preTasks.add(task);
                }
            }
        }
        return preTasks;
    }
    
    public void moveForItems(Person p, ITask task, SimulationMap map){
        System.out.print(p.name + " is fetching item:");
        for(String str: task.getRequiredItemsSet()){
            if(!p.hasItem(str, task.getRequiredItems().get(str))){
                p.setTargetItem(str);
                System.out.println(str);
                try {
                    p.setRoute(AStarMulti.getRoute(map.getLocationsOfItem(str), map.getClosestNode(p.currentLocation())));
                    if(map.getClosestNode(p.currentLocation()).equals(p.getRoute().getLast())) p.setRoute(null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public void setTaskForPerson(Person person, ITask task, SimulationMap map){
        System.out.println(person.name + " is doing task " + task.toString() + ", for " + person.getGoalTask().toString() + ", " + person.targetItem);
        try {
            Collection<Appliance> apps = map.getAppliances();
            Collection<String> valids = task.getUsedAppliances();
            Set<Appliance> validApps = new HashSet<>();
            for(Appliance app: apps){
                for(String valid: valids){
                    if(app.type().contains(valid)) validApps.add(app);
                }
            }
            System.out.println("Validapps: " + validApps.size() + ", Valids: " + valids.size());
            Collection<Node> goals = map.getLocationAppliances(valids);
            for(Person p: map.getPeople()){
                if(!p.equals(person) && p.getRoute() != null) goals.remove(p.getRoute().getLast());
            }
            Node closestNode = map.getClosestNode(person.currentLocation());
            person.setRoute(AStarMulti.getRoute(goals, closestNode));
            for(Appliance app: validApps){
                if(app.getLocation().equals(person.getRoute().getLast())){
                    person.setCurrentTask(task, app);
                    task.consumeItem(person);
                    break;
                }
            }
            if(closestNode.equals(person.getRoute().getLast())) person.setRoute(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public ITask taskForNeed(Need need, Collection<ITask> tasks, double time, Person person, SimulationMap map){
        List<ITask> tasksForNeed = new ArrayList<>();
        for(ITask task: tasks){
            if(task.fulfilledNeed() != null && task.fulfilledNeed().equals(need.name())){
                Collection<ITask> preTasks = tasksForGoal(task, person, map);
                if(allAvailable(preTasks, time)) tasksForNeed.add(task);
            }
        }
        tasksForNeed = new ArrayList<>(filterAvailable(tasksForNeed, time));
        System.out.println(tasksForNeed.size() + " possible tasks to fulfill " + need.name());
        return (tasksForNeed.isEmpty()? null: tasksForNeed.get(new Random().nextInt(tasksForNeed.size())));
    }
    
    public boolean allAvailable(Collection<ITask> tasks, double time){
        for(ITask task: tasks){
            if(!task.available(time)) return false;
        }
        return true;
    }
    
    public List<Need> getDesperateNeeds(List<Need> needs){
        List<Need> desperates = new ArrayList<>();
        for(Need need:needs){
            if(need.getValue() == 0.0) desperates.add(need);
        }
        return desperates;
    }
    
    public Need getLowestNeed(List<Need> needs){
        double value = 1000.0;
        Need returnedNeed = null;
        for(Need need: needs){
            if(value > need.getValue()){
                value = need.getValue();
                returnedNeed = need;
            }
        }
        return returnedNeed;
    }
    
    public Collection<ITask> filterAvailable(Collection<ITask> allTasks, double time){
        Collection<ITask> filteredTasks = new ArrayList<>();
        for(ITask task: allTasks){
            if(task.available(time)) filteredTasks.add(task);
        }
        return filteredTasks;
    }
}