
package no.oddsor.simulator3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
        if(person.getGoalTask() != null){
            if(person.getGoalTask().personHasAllItems(person)){
                person.setCurrentTask(person.getGoalTask());
                person.setGoalTask(null);
            }else{
                
            }
        }
        Collection<ITask> availableTasks = filterAvailable(tasks, time);
        List<Need> needs = new ArrayList<>(person.getNeeds());
        while(!needs.isEmpty()){
            Need lowest = getLowestNeed(needs);
            ITask taskForNeed = taskForNeed(lowest, availableTasks);
            if(taskForNeed != null){
                
            }
        }
        
        if(availableTasks.iterator().next().itemsExist(map)){
            person.setCurrentTask(availableTasks.iterator().next());
        }else{
            Set<String> items = availableTasks.iterator().next().getRequiredItems().keySet();
            person.setTargetItem(items.iterator().next());
        }
        //Find some task with no fulfilled need?
    }
    public ITask taskForNeed(Need need, Collection<ITask> tasks){
        List<ITask> tasksForNeed = new ArrayList<>();
        for(ITask task: tasks){
            if(task.fulfilledNeed().equals(need.name())) tasksForNeed.add(task);
        }
        return tasksForNeed.get(new Random().nextInt(tasksForNeed.size()));
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