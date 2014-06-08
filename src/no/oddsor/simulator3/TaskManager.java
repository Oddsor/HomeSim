
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
import no.oddsor.simulator3.json.TaskReader;
import no.oddsor.simulator3.planner.PPlanWrapper;
import no.oddsor.simulator3.tables.Node;


public class TaskManager {
    private static final Logger LOG = Logger.getLogger(TaskManager.class.getName());
    
    private final Collection<ITask> tasks;
    
    public TaskManager(TaskReader j){
        tasks = j.getTasks();
        
    }
    
    public void findTask(Person person, SimulationMap map, double time)
    {
        Collection<ITask> availableTasks = new ArrayList<>(filterAvailable(tasks, time));
        if(person.getGoalTask() != null && !person.getGoalTask().available(time)) person.setGoalTask(null);
        if(person.getGoalTask() != null)
        {
            if(person.getGoalTask().personMeetsRequirements(person))
            {
                LOG.info("Requirements met for task " + person.getGoalTask().name());
                setTaskForPerson(person, person.getGoalTask(), map);
                person.setGoalTask(null);
            }else if(person.getGoalTask().itemExists(person, map))
            {
                moveForItems(person, person.getGoalTask(), map);
            }else{
                try {
                    Deque<ITask> tusks = PPlanWrapper.getPlan(map, person, availableTasks, person.getGoalTask());
                    LOG.info(tusks.toString());
                    setTaskForPerson(person, tusks.getFirst(), map);
                } catch (Exception ex) {
                    ArrayList<String> mapItems = new ArrayList<>();
                    for(Item it: map.items){
                        mapItems.add(it.name);
                    }
                    LOG.log(Level.SEVERE, "Crashed when finding plan and setting task " + person.getGoalTask().name() + 
                            ", " + person.getGoalTask().getRequiredItemsSet() + ", " + person.getInventory().keySet() + ", " +
                            mapItems, ex);
                }
            }
        }else{
            //IF NO GOALTASK, WHAT THEN? CREATE A GOAL
            LOG.info("Finding a new goal task");
            List<Need> needs = new ArrayList<>(person.getNeeds());
            Collection<ITask> scheduledTasks = getScheduled(availableTasks);
            System.out.println(scheduledTasks);
            Collection<ITask> tasksToRemoveStuff = getRemoverTasks(availableTasks, person, map);
            System.out.println(tasksToRemoveStuff);
            if(scheduledTasks != null){
                person.setGoalTask(scheduledTasks.iterator().next());
            }else if(tasksToRemoveStuff != null){
                person.setGoalTask(tasksToRemoveStuff.iterator().next());
            }
            else{
                while(!needs.isEmpty()){
                    Need lowest = getLowestNeed(needs);
                    
                    LOG.fine(lowest.name() +  " is lowest need");
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
    }
    
    public void moveForItems(Person p, ITask task, SimulationMap map){
        LOG.info(p.name + " is fetching item:");
        for(String str: task.getRequiredItemsSet()){
            if(!p.hasItem(str, task.getRequiredItems().get(str)) && map.hasItem(str, task.getRequiredItems().get(str))){
                p.setTargetItem(str);
                LOG.info(str);
                try {
                    p.setRoute(AStarMulti.getRoute(map.getLocationsOfItem(str), map.getClosestNode(p.currentLocation())));
                    if(map.getClosestNode(p.currentLocation()).equals(p.getRoute().getLast())) p.setRoute(null);
                } catch (Exception ex) {
                    Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void setTaskForPerson(Person person, ITask task, SimulationMap map){
        if(task.itemExists(person, map)){
            moveForItems(person, task, map);
        }
        else{
            LOG.info(person.name + " is doing task " + task.toString() + ", for " + person.getGoalTask().toString());
            try {
                Collection<Appliance> apps = map.getAppliances();
                Collection<String> valids = task.getUsedAppliances();
                Set<Appliance> validApps = new HashSet<>();
                for(Appliance app: apps){
                    for(String valid: valids){
                        if(app.type().contains(valid)) validApps.add(app);
                    }
                }
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
                LOG.severe("Error while setting task. Route not found! Maybe " + task.getUsedAppliances().toString() + " does not exist or route is impossible?");
                person.setPauseTime(7200);
            }
        }
    }
    
    public ITask taskForNeed(Need need, Collection<ITask> tasks, double time, Person person, SimulationMap map){
        List<ITask> tasksForNeed = new ArrayList<>();
        for(ITask task: tasks){
            if(task.fulfilledNeed() != null && task.fulfilledNeed().equals(need.name())){
                try{
                    PPlanWrapper.getPlan(map, person, tasks, task);
                    tasksForNeed.add(task);
                }catch(Exception e){
                    LOG.info(task.name() + " has no viable plan");
                }
            }
        }
        LOG.info(tasksForNeed.size() + " possible tasks to fulfill " + need.name());
        return (tasksForNeed.isEmpty()? null: tasksForNeed.get(new Random().nextInt(tasksForNeed.size())));
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

    private Collection<ITask> getScheduled(Collection<ITask> availableTasks) {
        Collection<ITask> schedules = new ArrayList<>();
        for(ITask t: availableTasks){
            if(t.getType().equals("Scheduled")){
                schedules.add(t);
            }
        }
        if(schedules.isEmpty()) return null;
        else return schedules;
    }

    private Collection<ITask> getRemoverTasks(Collection<ITask> availableTasks, Person person, SimulationMap map) {
        Collection<ITask> removers = new ArrayList<>();
        Set<String> states = person.getState();
        for(ITask t: availableTasks){
            if(t.getType().equals("Cleanup") && t.itemsExist(person, map)) removers.add(t);
            else{
                for(String state: states){
                    if(t.getNeg().contains(state)) removers.add(t);
                }
            }

        }
        if(removers.isEmpty())return null;
        else return removers;
    }

    void passTime(double d) {
        for(ITask t: tasks){
            t.passTime(d);
        }
    }
}