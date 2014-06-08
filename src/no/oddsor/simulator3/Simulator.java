
package no.oddsor.simulator3;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.oddsor.AStarMulti.AStarMulti;
import no.oddsor.simulator3.tables.Node;


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
    public Simulator(SimulationMap map, TaskManager taskmanager, int simulationsPerSec){
        this.map = map;
        this.sensorlogger = null;
        this.simsPerSec = simulationsPerSec;
        this.time = new Time(0);
        this.currentTime = 0;
        this.taskManager = taskmanager;
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
            if(person.getPauseTime() > 0.0){
                person.passTime(1.0/simsPerSec);
                continue;
            }
            if(person.isMoving())
            {   //We're traveling!
                if(person.getType() == 0){ 
                    movement = true;
                    person.setLocation(map.moveActor(person, simsPerSec));
                }else{
                    person.setLocation(map.moveActor(person, simsPerSec * 6));
                }
            }
            else if(person.getType() == 0 && person.getTargetItem() != null)
            {
                person.progressFetch(1.0/simsPerSec);
                Node current = map.getClosestNode(person.currentLocation());
                if(person.getFetchTime() <= 0.0){
                    Item fetchedItem = map.popItem(person.getTargetItem(), current);
                    while(fetchedItem != null){ 
                        person.addItem(fetchedItem);
                        fetchedItem = map.popItem(fetchedItem.name, current);
                        if(fetchedItem == null){
                            if(map.hasItem(person.getTargetItem(), 1)){
                                taskManager.moveForItems(person, (person.getGoalTask() != null?person.getGoalTask():(person.getCurrentTask() != null?person.getCurrentTask():null)), map);
                            }
                        }
                    }person.setTargetItem(null);
                }
            }
            else if(person.getType() == 0 && person.getCurrentTask() != null)
            {
                person.progressTask(1.0/simsPerSec);
                if(person.remainingDuration() <= 0.0){
                    if(person.getCurrentTask().getType().equals("Automatic")){
                        map.addTask(person.getCurrentTask(), map.getClosestNode(person.currentLocation()));
                        for(String stat: person.getCurrentTask().getNeg()){
                            person.addState("-" + stat);
                        }for(String stat: person.getCurrentTask().getPos()){
                            person.addState("+" + stat);
                        }
                        
                    }else{
                        person.getCurrentTask().completeTask(person, map);
                        person.getCurrentTask().recentlyCompleted();
                    }
                    person.setCurrentTask(null, null);
                }
            }
            else{
                if(Math.random() < 0.15 && person.getPauseTime() == 0.0)
                {
                    try {
                        person.setRoute(AStarMulti.getRoute(map.getRandomNode(), map.getClosestNode(person.currentLocation())));
                    } catch (Exception ex) {
                        Logger.getLogger(Simulator.class.getName()).warning("No route found");
                    }
                }else if(person.getType() == 0){
                    taskManager.findTask(person, map, currentTime);
                }
            }
            sensorlogger.log(person, map.getSensors(), currentTime);
            person.passTime(1.0/simsPerSec);
            
        }
        taskManager.passTime(1.0/simsPerSec);
        map.progressTasks(1.0/simsPerSec);
        currentTime += (1.0/simsPerSec);
        return movement;
    }
}