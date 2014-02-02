
package no.oddsor.simulator3;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Odd
 */
public class Simulator {
    
    ArrayList<Person> people;
    ArrayList<HouseObjects> objects;
    SimulationMap map;
   
    Map<String, Integer> variables;
    
    private int simsPerSec;
    private final int walkingSpeed = (int) (map.walkingSpeedPerSec / simsPerSec);
    
    private long time = 0;
    
    //TODO make resumable
    public Simulator(boolean restart, SimulationMap map, ArrayList<Person> people, 
            ArrayList<HouseObjects> objects, int simulationsPerSec){
        this.people = people;
        this.map = map;
        this.objects = objects;
        this.simsPerSec = simulationsPerSec;
        variables = new HashMap<>();
    }
    
    public void simulationStep(){
        for(Person person: people){
            ArrayList<Node> route = person.getRoute();
            if(route != null){ //We're traveling!
                person.setLocation(moveToPoint(person.currentLocation(), route.get(0)));
            }else{ //TODO else if not doing target task, do task
                person.setTask(getNextTask(person.getSortedNeeds()), map);
            }
            person.passTime(1/simsPerSec);
        }
        time += (1/simsPerSec);
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
    
    private Task getNextTask(ArrayList<Need> sortedNeeds){
        Task task = null;
        for(Need need: sortedNeeds){
            
        }
        return task;
    }
    
}
