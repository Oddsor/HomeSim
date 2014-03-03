
package no.oddsor.simulator3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private TaskManager taskManager;
    
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
                if(person.remainingDuration() <= 0.0) person.setCurrentTask(null);
            }else if(person.getTargetItem() != null){
                if(person.getFetchTime() > 0.0) continue;
                Item fetchedItem = map.popItem(person.getTargetItem(), map.getClosestNode(person.currentLocation()));
                if(fetchedItem != null){ person.addItem(fetchedItem);
                    if(person.hasItem(fetchedItem.name, 
                            person.getCurrentTask().getRequiredItems().get(fetchedItem.name))){
                        person.setTargetItem(null);
                        for(String itemName: person.getCurrentTask().getRequiredItems().keySet()){
                            if(!person.hasItem(itemName, person.getCurrentTask().getRequiredItems().get(itemName))){
                                person.setTargetItem(itemName);
                            }
                        }
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