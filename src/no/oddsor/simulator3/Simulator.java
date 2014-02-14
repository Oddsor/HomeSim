
package no.oddsor.simulator3;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Odd
 */
public class Simulator {
    
    ArrayList<Person> people;
    SimulationMap map;
    ArrayList<Task> tasks;
    Set<Node> blockedNodes;
   
    //Map<String, Integer> variables;
    
    private int simsPerSec;
    private int walkingSpeed;
    public Time time;
    public double currentTime;
    
    public Simulator(boolean restart, SimulationMap map, ArrayList<Person> people, int simulationsPerSec){
        this.people = people;
        this.map = map;
        this.simsPerSec = simulationsPerSec;
        this.time = new Time(0);
        this.currentTime = 0;
        this.blockedNodes = new HashSet<>();
        this.walkingSpeed = (int) (map.walkingSpeedPerSec / simsPerSec);
        System.out.println("walkspeed: " + walkingSpeed);
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
    
    private Point moveToPoint(Point currentLocation, Node targetNode){
        Point p = new Point(currentLocation);
        double distance = p.distance(currentLocation);
        Point targetLocation = targetNode.getLocation();
        int dx = p.x - targetLocation.x;
        int dy = p.y - targetLocation.y;
        if(distance < walkingSpeed) p.setLocation(targetLocation);
        else{
            p.translate((int) (dx * (walkingSpeed / distance)), 
                    (int) (dy * (walkingSpeed / distance)));
        }
        return p;
    }
    
    private Task getNextTask(Person p){
        List<Need> needs = p.getSortedNeeds();
        Task task = null;
        ArrayList<Task> filtered = filterAvailableTasks(tasks);
        ArrayList<Task> goalTasks = getGoalTasks(filtered);
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
    
    public ArrayList<Person> getPeople(){
        return people;
    }
}
