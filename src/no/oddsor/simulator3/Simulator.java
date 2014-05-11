
package no.oddsor.simulator3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.oddsor.AStarMulti.AStarMulti;
import no.oddsor.simulator3.json.JSON;
import no.oddsor.simulator3.json.SensorReader;
import no.oddsor.simulator3.sensor.Camera;
import no.oddsor.simulator3.sensor.Sensor;
import no.oddsor.simulator3.sensor.SensorArea;


public class Simulator {
    
    Collection<Person> people;
    Collection<Sensor> sensors;
    SimulationMap map;
    ArrayList<Task> tasks;
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
        for(Person person: people)
        {
            if(person.isMoving())
            {   //We're traveling!
                person.setLocation(map.moveActor(person, simsPerSec));
                movement = true;
            }
            else if(person.getCurrentTask() != null)
            {
                person.progressTask(1.0/simsPerSec);
                if(person.remainingDuration() <= 0.0){
                    person.getCurrentTask().completeTask(person, map);
                    person.setCurrentTask(null, null);
                }
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
            for(String sa: recentlyTriggered.keySet()){
                double oldVal = recentlyTriggered.get(sa);
                double newVal = oldVal - 1.0/simsPerSec;
                if(newVal < 0.0){
                    newVal = 0.0;
                    /*if(oldVal > 0.0){
                        try {
                            sensorlogger.addSensor(currentTime, sa,
                                sa, "OFF",
                                (person.getCurrentTask() != null ? person.getCurrentTask().name() : "Other_Activity"));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }*/
                }
                recentlyTriggered.put(sa, newVal);
            }
            for(Sensor s: sensors){
                for(SensorArea sa: s.getSensorAreas()){
                    if(sa.getArea() == null && person.getUsingAppliance() != null 
                            && person.getRoute() == null && person.getCurrentTask() != null
                            && sa.getName().contains(person.getUsingAppliance().type) &&
                            (recentlyTriggered.containsKey(person.getUsingAppliance().getName()) && recentlyTriggered.get(person.getUsingAppliance().getName()) <= 0.0)){
                        System.out.println(sa.getName() + ", " + sa.getName().equals(person.getUsingAppliance().type));
                        System.out.println(person.getCurrentTask().name() + ", " + person.getUsingAppliance().type);
                        recentlyTriggered.put(person.getUsingAppliance().getName(), 5.0);
                        try {
                            sensorlogger.addSensor(currentTime, person.getUsingAppliance().getName(),
                                    person.getUsingAppliance().getName(), "ON", person.getCurrentTask().name());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else if(sa.getArea() != null){
                        if(sa.getArea().contains(person.currentLocation())){
                            if(s instanceof Camera){
                                Set<String> poses = person.getPoseData();
                                for(String pose:poses){
                                    if(!recentlyTriggered.containsKey(sa.getName() + pose) ||
                                            (recentlyTriggered.get(sa.getName() + pose) != null && 
                                            recentlyTriggered.get(sa.getName() + pose) <= 0.0)){
                                        recentlyTriggered.put(sa.getName() + pose, 20.0);
                                        try{
                                            sensorlogger.addSensor(currentTime, sa.getName(), sa.getName(), pose, 
                                                    (person.getCurrentTask() != null ? person.getCurrentTask().name() : "Other_Activity"));
                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            if(!recentlyTriggered.containsKey(sa.getName()) || 
                                    (recentlyTriggered.get(sa.getName()) != null && recentlyTriggered.get(sa.getName()) <= 0.0)){
                                recentlyTriggered.put(sa.getName(), 5.0);
                                try {
                                    sensorlogger.addSensor(currentTime, sa.getName(),
                                            sa.getName(), "ON",
                                            (person.getCurrentTask() != null ? person.getCurrentTask().name() : "Other_Activity"));
                                } catch (IOException ex) {
                                    Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
                                    ex.printStackTrace();
                                }
                            }
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