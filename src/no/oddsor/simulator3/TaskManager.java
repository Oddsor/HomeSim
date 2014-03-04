
package no.oddsor.simulator3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.oddsor.AStarMulti.AStarMulti;
import no.oddsor.simulator3.json.JSON;

/**
 *
 * @author Odd
 */
public class TaskManager {
    
    private JSON json;
    private Collection<ITask> tasks;
    
    public TaskManager(JSON j){
        this.json = j;
        tasks = j.getTasks();
    }
    
    public void findTask(Person person, SimulationMap map, double time){
        Collection<ITask> availableTasks = new ArrayList<>(filterAvailable(tasks, time));
        if(person.getGoalTask() != null && !person.getGoalTask().available(time)) person.setGoalTask(null);
        if(person.getGoalTask() != null){
            if(person.getGoalTask().personHasAllItems(person)){
                setTaskForPerson(person, person.getGoalTask(), map);
                person.setGoalTask(null);
            }else{
                if(person.getGoalTask().itemsExist(person, map)){
                    moveForItems(person, person.getGoalTask(), map);
                }else{
                    findTaskLoop(person.getGoalTask(), person, map);
                }
            }
        }else{
            //IF NO GOALTASK, WHAT THEN? CREATE A GOAL
            List<Need> needs = new ArrayList<>(person.getNeeds());
            while(!needs.isEmpty()){
                Need lowest = getLowestNeed(needs);
                ITask taskForNeed = taskForNeed(lowest, availableTasks);
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
        if(currentTask.personHasAllItems(person)) setTaskForPerson(person, currentTask, map);
        else if(currentTask.itemsExist(person, map)){
            System.out.println("Yay, items found for " + currentTask.toString());
            moveForItems(person, currentTask, map);
        }
        else{
            System.out.println(currentTask.getRequiredItemsSet().toString());
            Collection<ITask> prereqTasks = tasksForGoal(currentTask);
            for(ITask task: prereqTasks){
                findTaskLoop(task, person, map);
                if(person.targetItem != null || person.getCurrentTask() != null) break;
            }
        }
    }
    
    public Collection<ITask> tasksForGoal(ITask goal){
        Collection<ITask> preTasks = new ArrayList<>();
        for(ITask task: tasks){
            for(String str: task.getCreatedItems()){
                if(goal.getRequiredItemsSet().contains(str)) preTasks.add(task);
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
        person.setCurrentTask(task);
        System.out.println(person.name + " is doing task " + task.toString() + ", for " + person.getGoalTask().toString() + ", " + person.targetItem);
        task.consumeItem(person);
        try {
            Collection<Node> goals = map.getLocationAppliances(task.getUsedAppliances());
            for(Person p: map.getPeople()){
                if(!p.equals(person) && p.getRoute() != null) goals.remove(p.getRoute().getLast());
            }
            person.setRoute(AStarMulti.getRoute(goals, map.getClosestNode(person.currentLocation())));
            if(map.getClosestNode(person.currentLocation()).equals(person.getRoute().getLast())) person.setRoute(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public ITask taskForNeed(Need need, Collection<ITask> tasks){
        List<ITask> tasksForNeed = new ArrayList<>();
        for(ITask task: tasks){
            if(task.fulfilledNeed() != null && task.fulfilledNeed().equals(need.name())) tasksForNeed.add(task);
        }
        return (tasksForNeed.isEmpty()? null: tasksForNeed.get(new Random().nextInt(tasksForNeed.size())));
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