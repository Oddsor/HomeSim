/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3.json;

import java.awt.Shape;
import java.awt.geom.Area;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Odd
 */
public class SensorReader {
    
    private JSONObject object;
    
    public SensorReader(String fileName) throws Exception{
        JSONParser jp = new JSONParser();
        this.object = (JSONObject) jp.parse(new FileReader("tasks.json"));
    }
    
    public Collection<Sensor> getSensors(){
        Collection<Sensor> sensors = new ArrayList<>();
        
        
        
        return sensors;
    }
    
}

class Sensor extends Area{
    private final String name;
    private final boolean camera;
    public Sensor(String name, boolean camera, Shape shape){
        super(shape);
        this.camera = camera;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isCamera() {
        return camera;
    }
}
