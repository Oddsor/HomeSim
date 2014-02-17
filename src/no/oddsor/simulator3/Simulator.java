
package no.oddsor.simulator3;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Odd
 */
public class Simulator {
    
    Collection<Person> people;
    SimulationMap map;
    ArrayList<Task> tasks;
    //Set<Node> blockedNodes;
    
    int simsPerSec;
    int walkingSpeed;
    public Time time;
    public double currentTime;
    
    /**
     *
     * @param map
     * @param people
     * @param simulationsPerSec
     */
    public Simulator(SimulationMap map, Collection<Person> people, int simulationsPerSec){
        this.people = people;
        this.map = map;
        this.simsPerSec = simulationsPerSec;
        this.time = new Time(0);
        this.currentTime = 0;
        //this.blockedNodes = new HashSet<>();
        this.walkingSpeed = (int) (map.walkingSpeedPerSec / simsPerSec);
        this.tasks = TaskSingleton.getTaskList();
    }
    
    public void simulationStep(){
        for(Person person: people){
            Queue<Node> route = person.getRoute();
            if(route != null){ //We're traveling!
                person.setLocation(moveToPoint(person.currentLocation(), route.peek()));
            }else if(person.getTask() == null){ //TODO else if not doing target task, do task
                person.setTask(getNextTask(person), map);
            }
            person.passTime(1.0/simsPerSec);
        }
        currentTime += (1.0/simsPerSec);
    }
    
    //TODO verify
    private Point moveToPoint(Point currentLocation, Node targetNode){
        Point p = new Point(currentLocation);
        Point targetLocation = targetNode.getLocation();
        double distance = p.distance(targetLocation);
        int dx = targetLocation.x - p.x;
        int dy = targetLocation.y - p.y;
        System.out.println("DX: " + dx + ", DY:" + dy + ", Distance: " + distance);
        if(distance < walkingSpeed) p.setLocation(targetLocation);
        else{
            p.translate((int) (walkingSpeed * (dx / distance)), 
                    (int) (walkingSpeed * (dy / distance)));
        }
        return p;
    }
    
    private Task getNextTask(Person p){
        List<Need> needs = p.getNeeds();
        Task task = null;
        ArrayList<Task> goalTasks = getGoalTasks(filterAvailableTasks(tasks));
        for(Need need: needs){
            if(need.getValue() > 60) continue;
            for(Task fTask: goalTasks){
                if(fTask.fulfilledNeed != null && fTask.fulfilledNeed == need.type() 
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