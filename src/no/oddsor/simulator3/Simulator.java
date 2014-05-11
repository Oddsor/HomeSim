
package no.oddsor.simulator3;

import java.util.HashMap;
import java.util.Map;
import net.oddsor.AStarMulti.AStarMulti;
import no.oddsor.simulator3.json.TaskReader;


public class Simulator {
    
    SimulationMap map;
    Map<String, Double> recentlyTriggered;
    
    int simsPerSec;
    public Time time;
    public double currentTime;
    private final TaskManager taskManager;
    private SensorLogger sensorlogger;
    
    /**
     *
     * @param map
     * @param simulationsPerSec
     */
    public Simulator(SimulationMap map, int simulationsPerSec){
        this.map = map;
        this.sensorlogger = null;
        this.simsPerSec = simulationsPerSec;
        this.time = new Time(0);
        this.currentTime = 0;
        TaskReader j = null;
        try {
            j = new TaskReader("tasks.json");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.taskManager = new TaskManager(j);
        this.recentlyTriggered = new HashMap<>();
    }
    
    public void setSensorLogger(SensorLogger sl){
        this.sensorlogger = sl;
    }
    
    /**
     * 
     * @return True if any player moved
     */
    public boolean simulationStep()
    {
        boolean movement = false;
        for(Person person: map.getPeople())
        {
            if(person.isMoving())
            {   //We're traveling!
                person.setLocation(map.moveActor(person, simsPerSec));
                movement = true;
            }
            else if(person.getTargetItem() != null)
            {
                person.progressFetch(1.0/simsPerSec);
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
            }
            else if(person.getCurrentTask() != null)
            {
                person.progressTask(1.0/simsPerSec);
                if(person.remainingDuration() <= 0.0){
                    person.getCurrentTask().completeTask(person, map);
                    person.getCurrentTask().recentlyCompleted();
                    person.setCurrentTask(null, null);
                }
            }
            else{
                if(Math.random() < 0.25)
                {
                    try {
                        System.out.println(person.name + " chooses to wander a bit.");
                        person.setRoute(AStarMulti.getRoute(map.getRandomNode(), map.getClosestNode(person.currentLocation())));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }else{
                    taskManager.findTask(person, map, currentTime);
                }
            }
            sensorlogger.log(person, map.getSensors(), currentTime);
            person.passTime(1.0/simsPerSec);
            taskManager.passTime(1.0/simsPerSec);
        }
        currentTime += (1.0/simsPerSec);
        return movement;
    }
}