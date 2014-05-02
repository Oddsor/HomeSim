
package no.oddsor.simulator3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.oddsor.AStarMulti.AStarMulti;
import no.oddsor.simulator3.json.JSON;
import no.oddsor.simulator3.json.SensorReader;
import no.oddsor.simulator3.sensor.Sensor;
import no.oddsor.simulator3.sensor.SensorArea;

/**
 *
 * @author Odd
 */
public class Simulator {
    
    Collection<Person> people;
    Collection<Sensor> sensors;
    SimulationMap map;
    ArrayList<Task> tasks;
    Map<SensorArea, Double> recentlyTriggered;
    
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
        try {
            sensors = new SensorReader("sensors.json").getSensors();
        } catch (Exception ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.taskManager = new TaskManager(j);
        this.recentlyTriggered = new HashMap<>();
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
                if(Math.random() < 0.25){
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
            for(SensorArea sa: recentlyTriggered.keySet()){
                double newVal = recentlyTriggered.get(sa) - 1.0/simsPerSec;
                if(newVal < 0.0) newVal = 0.0;
                recentlyTriggered.put(sa, newVal);
            }
            for(Sensor s: sensors){
                if(s.getSensorAreas() == null){
                    //IS CONTACT SENSOR
                    //TODO trigger by using appliance
                }
                for(SensorArea sa: s.getSensorAreas()){
                    if(sa.getArea().contains(person.currentLocation())){
                        if(!recentlyTriggered.containsKey(sa) || (recentlyTriggered.get(sa) != null && recentlyTriggered.get(sa) < 0.0)){
                            recentlyTriggered.put(sa, 5.0);
                            //TODO ADD save to database
                        }
                    }
                }
            }
            person.passTime(1.0/simsPerSec);
        }
        currentTime += (1.0/simsPerSec);
        return movement;
    }
    
    public Collection<Person> getPeople(){
        return people;
    }
    
    public Collection<Sensor> getSensors(){
        return sensors;
    }
}