/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3.json;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import no.oddsor.simulator3.ITask;
import no.oddsor.simulator3.Person;
import no.oddsor.simulator3.Task;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Odd
 */
public class JSON {
    
    public final JSONObject object;
    
    public JSON(String filename) throws Exception{
        JSONParser jp = new JSONParser();
        this.object = (JSONObject)jp.parse(new FileReader("tasks.json"));
    }
    
    public Collection<ITask> getTasks(){
        Collection<ITask> tasks = new  ArrayList<>();
        JSONArray tasklist = (JSONArray) object.get("Tasks");
        Iterator taskIterator = tasklist.iterator();
        while(taskIterator.hasNext()){
            JSONObject nextTask = (JSONObject) taskIterator.next();
            Task newTask;
            if(nextTask.containsKey("Timespan")){
                JSONArray timespan = (JSONArray) nextTask.get("Timespan");
                tasks.add(newTask = new Task((String) nextTask.get("Name"), 
                        (String) nextTask.get("Type"), 
                        Integer.parseInt(nextTask.get("Duration").toString()),
                        Integer.parseInt(timespan.get(0).toString()), 
                        Integer.parseInt(timespan.get(1).toString()), null));
            }else{
                tasks.add(newTask = new Task((String) nextTask.get("Name"), 
                        (String) nextTask.get("Type"), 
                        Integer.parseInt(nextTask.get("Duration").toString()), null));
            }
            if(nextTask.containsKey("IncreasesNeed")){
                JSONArray need = (JSONArray) nextTask.get("IncreasesNeed");
                newTask.addResult((String) need.get(0), 
                        Integer.parseInt(need.get(1).toString()));
            }if(nextTask.containsKey("Requires")){
                if(!(nextTask.get("Requires") instanceof JSONArray)){
                    newTask.addRequiredItem((String) nextTask.get("Requires"), 1);
                }else{
                    JSONArray requirements = (JSONArray) nextTask.get("Requires");
                    if(requirements.get(1) instanceof Number){
                        newTask.addRequiredItem((String) requirements.get(0), 
                                Integer.parseInt(requirements.get(1).toString()));
                    }else{
                        for(Object item: requirements){
                            newTask.addRequiredItem((String) item, 1);
                        }
                    }
                }
            }if(nextTask.containsKey("Creates")){
                if(nextTask.get("Creates") instanceof JSONArray){
                    JSONArray creates = (JSONArray) nextTask.get("Creates");
                    newTask.addResultingItem((String) creates.get(0), 
                            Integer.parseInt(creates.get(1).toString()));
                }else newTask.addResultingItem((String) nextTask.get("Creates"), 1);
            }
        }
        return tasks;
    }
    
    public Set<String> getAppliances(){
        Set<String> appliances = new HashSet<>();
        JSONArray tasklist = (JSONArray) object.get("Tasks");
        Iterator tasks = tasklist.iterator();
        while(tasks.hasNext()){
            JSONObject task = (JSONObject) tasks.next();
            if(task.containsKey("Appliance")) appliances.add((String) task.get("Appliance"));
            if(task.containsKey("Creates") && task.get("Creates") instanceof JSONArray){
                JSONArray createArray = (JSONArray) task.get("Creates");
                if(createArray.get(1) instanceof String)appliances.add(createArray.get(1).toString());
            }
        }
        return appliances;
    }
    
    public Collection<Person> getPeople(){
        JSONArray peopleMap = (JSONArray) object.get("People");
        Collection<Person> people = new ArrayList<>();
        for(int i = 0; i < peopleMap.size(); i++){
            JSONObject personObject = (JSONObject) peopleMap.get(i);
            Person p = new Person((String) personObject.get("Name"), 
                    (String) personObject.get("Image"), null);
            people.add(p);
        }
        return people;
    }
    
    public static void main(String[] args){
        try {
            JSON j = new JSON("tasks.json");
            System.out.println(j.object.toJSONString());
            System.out.println(j.getAppliances().toString());
            System.out.println(j.getPeople().iterator().next().name);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
