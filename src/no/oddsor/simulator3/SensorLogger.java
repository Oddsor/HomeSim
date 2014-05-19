/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.oddsor.simulator3.sensor.Camera;
import no.oddsor.simulator3.sensor.Sensor;
import no.oddsor.simulator3.sensor.SensorArea;


public class SensorLogger {
    private static final Logger LOG = Logger.getLogger(SensorLogger.class.getName());

    private BufferedWriter fileWriter;
    private final DecimalFormat df;
    private int counter;
    private int suffix;
    private final String fileName;
    private final Map<String, Double> lastTriggered;
    private Set<String> lastPoses;
    private ITask lastTask;
    
    private final double TRIGGER_INTERVAL = 5.0;
    
    public SensorLogger(String fileName) throws IOException{
        this.fileName = fileName;
        this.suffix = 1;
        this.fileWriter = new BufferedWriter(new FileWriter(fileName+suffix, true));
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df = new DecimalFormat("##.#####", dfs);
        df.setMinimumIntegerDigits(2);
        df.setMaximumIntegerDigits(2);
        df.setMinimumFractionDigits(6);
        df.setMaximumFractionDigits(6);
        counter = 0;
        lastTriggered = new HashMap<String, Double>();
        lastPoses = null;
        lastTask = null;
    }
    
    public void log(Person person, Collection<Sensor> sensors, double currentTime){
        
        for(Sensor s: sensors){
            for(SensorArea sa: s.getSensorAreas()){
                if(sa.getArea() == null && person.getUsingAppliance() != null 
                        && !person.isMoving() && person.getCurrentTask() != null
                        && sa.getName().contains(person.getUsingAppliance().type) &&
                        (!lastTriggered.containsKey(person.getUsingAppliance().getName()) || 
                        (lastTriggered.get(person.getUsingAppliance().getName()) != null && currentTime - lastTriggered.get(person.getUsingAppliance().getName()) >= 60.0))){
                    lastTriggered.put(person.getUsingAppliance().getName(), currentTime);
                    if(!sa.getLastValue().equals("ON")){
                        try {
                            addSensor(currentTime, person.getUsingAppliance().getName()+"CT",
                                    person.getUsingAppliance().getName()+"CT", "ON", (person.getCurrentTask() != null ? 
                                                        (person.getCurrentTask().label() != null ? person.getCurrentTask().label() : 
                                                                (person.getGoalTask() != null ? 
                                                                        (person.getGoalTask().label() != null ? person.getGoalTask().label() : "Other_Activity")
                                                                    : "Other_Activity"))
                                                        : "Other_Activity"));
                            sa.setLastValue("ON");
                        } catch (IOException ex) {
                            LOG.log(Level.SEVERE, null, ex);
                        }
                    }
                }else if(sa.getArea() != null && sa.getArea().contains(person.currentLocation())){
                    if(s instanceof Camera && !person.isMoving()){
                        Set<String> poses = person.getPoseData();
                        if(lastPoses != poses && lastTask != person.getCurrentTask()){
                            lastPoses = poses;
                            lastTask = person.getCurrentTask();
                            for(String pose:poses){
                                if(!lastTriggered.containsKey(sa.getName() + pose) ||
                                        (lastTriggered.get(sa.getName() + pose) != null && 
                                        currentTime - lastTriggered.get(sa.getName() + pose)  >= 10.0)){
                                    lastTriggered.put(sa.getName() + pose, currentTime);
                                    try{
                                        addSensor(currentTime, sa.getName(), sa.getName(), pose, 
                                                (person.getCurrentTask() != null ? 
                                                        (person.getCurrentTask().label() != null ? person.getCurrentTask().label() : 
                                                                (person.getGoalTask() != null ? 
                                                                        (person.getGoalTask().label() != null ? person.getGoalTask().label() : "Other_Activity")
                                                                    : "Other_Activity"))
                                                        : "Other_Activity"));
                                    }catch(IOException ex){
                                        LOG.log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }
                    }
                    if(!lastTriggered.containsKey(sa.getName()) || 
                            (lastTriggered.get(sa.getName()) != null && currentTime - lastTriggered.get(sa.getName())  >= TRIGGER_INTERVAL)){
                        lastTriggered.put(sa.getName(), currentTime);
                        if(!sa.getLastValue().equals("ON")){
                            try {
                                addSensor(currentTime, sa.getName(),
                                        sa.getName(), "ON",
                                        (person.getCurrentTask() != null ? 
                                                    (person.getCurrentTask().label() != null ? person.getCurrentTask().label() : 
                                                            (person.getGoalTask() != null ? 
                                                                    (person.getGoalTask().label() != null ? person.getGoalTask().label() : "Other_Activity")
                                                                : "Other_Activity"))
                                                    : "Other_Activity"));
                                sa.setLastValue("ON");
                            } catch (IOException ex) {
                                LOG.log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }else{
                    //Not still in area.
                    if(lastTriggered.containsKey(sa.getName()) && currentTime - lastTriggered.get(sa.getName()) >= TRIGGER_INTERVAL){
                        if(!sa.getLastValue().equals("OFF")){
                            try {
                                sa.setLastValue("OFF");
                            } catch (Exception ex) {
                                LOG.log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void addSensor(double time, String sensorName, String sensorNote, 
            String sensorValue, String activityName) throws IOException{
        
        int day = Time.getDay(time);
        int dayOfMonth = day % 30;
        int month = day / 30;
        int currentMonth = month % 11 + 1;
        int year = 2014 + (month / 12);
        fileWriter.append(year+ "-" +Time.getNumberFormatted(currentMonth)+"-"+
                Time.getNumberFormatted(dayOfMonth) + " " +
                Time.getNumberFormatted(Time.getHours(time)) + ":" + 
                Time.getNumberFormatted(Time.getMinutes(time)) + ":" + 
                df.format(time % 60)
                +" "+removeSpaces(sensorName) + " " + removeSpaces(sensorNote)
                + " " + removeSpaces(sensorValue) + " " + 
                removeSpaces(activityName) + "\n");
        fileWriter.flush();
        /*if(counter == 25000){
            counter = 0;
            suffix ++;
            fileWriter.close();
            this.fileWriter = new BufferedWriter(
                    new FileWriter(fileName+suffix, true));
        }counter++;*/
    }
    public String removeSpaces(String text){
        return text.replace(' ', '_');
    }
    
}
