
package no.oddsor.simulator3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Odd
 */
public class Simulator {
    
    Collection<Person> people;
    SimulationMap map;
    ArrayList<Task> tasks;
    
    int simsPerSec;
    public Time time;
    public double currentTime;
    
    /**
     *
     * @param map
     * @param people
     * @param simulationsPerSec
     */
    public Simulator(SimulationMap map, int simulationsPerSec){
        this.map = map;
        this.people = map.getPeople();
        this.simsPerSec = simulationsPerSec;
        this.time = new Time(0);
        this.currentTime = 0;
        this.tasks = TaskSingleton.getTaskList();
    }
    
    /**
     * 
     * @return True if any player moved
     */
    public boolean simulationStep(){
        boolean movement = false;
        for(Person person: people){
            Queue<Node> route = person.getRoute();
            if(route != null){ //We're traveling!
                person.setLocation(map.moveActor(person, simsPerSec));
                movement = true;
            }else if(person.getTask() != null){ //TODO else if not doing target task, do task
                person.getTask().progressTask(1.0/simsPerSec);
                if(person.getTask().remainingDuration() <= 0.0) person.setTask(null);
            }else{
                person.setTask(taskmanager.findTask(person, map, time));
            }
            person.passTime(1.0/simsPerSec);
        }
        currentTime += (1.0/simsPerSec);
        return movement;
    }
    
    private Task getNextTask(Person p){
        List<Need> needs = p.getNeeds();
        Task task = null;
        ArrayList<Task> goalTasks = getGoalTasks(filterAvailableTasks(tasks));
        for(Need need: needs){
            if(need.getValue() > 60) continue;
            for(Task fTask: goalTasks){
                if(fTask.fulfilledNeed != null && fTask.fulfilledNeed.equals(need.name()) 
                        && fTask.completable(p, map.objects)){
                    task = fTask;
                }
            }
        }
        return task;
    }
    
    private ArrayList<Task> filterAvailableTasks(ArrayList<Task> allTasks){
        ArrayList<Task> filteredTasks = new ArrayList<>();
        
        for(Task task: allTasks){
            if(task.taskAvailable(time.getDay(currentTime), time.getHours(currentTime))) filteredTasks.add(task);
        }
        return filteredTasks;
    }
    
    private ArrayList<Task> getGoalTasks(ArrayList<Task> allTasks){
        ArrayList<Task> goalTasks = new ArrayList<>();
        for(Task task: allTasks){
            if(task.fulfilledNeed == null) continue;
            goalTasks.add(task);
        }
        return goalTasks;
    }
    
    public Collection<Person> getPeople(){
        return people;
    }
}