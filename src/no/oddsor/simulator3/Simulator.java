
package no.oddsor.simulator3;

import java.util.ArrayList;
import java.util.Collection;
import no.oddsor.simulator3.json.JSON;

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
    private final TaskManager taskManager;
    
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
        JSON j = null;
        try {
            j = new JSON("tasks.json");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.taskManager = new TaskManager(j);
    }
    
    /**
     * 
     * @return True if any player moved
     */
    public boolean simulationStep(){
        boolean movement = false;
        for(Person person: people){
            if(person.getRoute() != null){ //We're traveling!
                person.setLocation(map.moveActor(person, simsPerSec));
                movement = true;
            }else if(person.getCurrentTask() != null){
                person.progressTask(1.0/simsPerSec);
                if(person.remainingDuration() <= 0.0){
                    person.getCurrentTask().completeTask(person, map);
                    person.setCurrentTask(null);
                }
            }else if(person.getTargetItem() != null){
                person.progressFetch(1.0/simsPerSec);
                System.out.println("Fetching..");
                if(person.getFetchTime() <= 0.0){
                    Item fetchedItem = map.popItem(person.getTargetItem(), map.getClosestNode(person.currentLocation()));
                    System.out.println(fetchedItem.name);
                    if(fetchedItem != null){ 
                        person.addItem(fetchedItem);
                        person.setTargetItem(null);
                    }else{
                        System.out.println("Trouble fetching item??");
                    }
                }
            }else{
                taskManager.findTask(person, map, currentTime);
            }
            person.passTime(1.0/simsPerSec);
        }
        currentTime += (1.0/simsPerSec);
        return movement;
    }
    
    public Collection<Person> getPeople(){
        return people;
    }
}