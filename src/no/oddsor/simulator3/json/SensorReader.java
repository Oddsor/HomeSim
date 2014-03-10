/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3.json;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import no.oddsor.simulator3.sensor.Camera;
import no.oddsor.simulator3.sensor.Door;
import no.oddsor.simulator3.sensor.MotionSensor;
import no.oddsor.simulator3.sensor.Sensor;
import org.json.simple.JSONArray;
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
        this.object = (JSONObject) jp.parse(new FileReader("sensors.json"));
    }
    
    public Collection<Sensor> getSensors(){
        Collection<Sensor> sensors = new ArrayList<>();
        
        JSONArray sensorList = (JSONArray) object.get("Sensors");
        for(Object sensorOb: sensorList){
            JSONObject sensorObject = (JSONObject) sensorOb;
            Sensor sensor = null;
            String type = (String) sensorObject.get("Type");
            String name = (String) sensorObject.get("Name");
            JSONArray locationArray = (JSONArray) sensorObject.get("Position");
            Point location = new Point(Integer.parseInt(locationArray.get(0).toString()), 
                Integer.parseInt(locationArray.get(1).toString()));
            switch(type){
                case "Camera": 
                    JSONArray range = (JSONArray) sensorObject.get("Range");
                    double[] dArray = new double[range.size()];
                    for(int i = 0; i < dArray.length; i++){
                        dArray[i] = Double.parseDouble(range.get(i).toString());
                    }
                    if(!sensorObject.containsKey("Resolution")) sensor = new Camera(name, 
                        location, Double.parseDouble(sensorObject.get("Direction").toString()), 
                            Double.parseDouble(sensorObject.get("FieldOfView").toString()), 
                        dArray);
                    else sensor = new Camera(name, 
                        location, Double.parseDouble(sensorObject.get("Direction").toString()), 
                            Double.parseDouble(sensorObject.get("FieldOfView").toString()), 
                        dArray, Integer.parseInt(sensorObject.get("Resolution").toString()));
                    break;
                case "Door":
                    JSONArray dimensionArray = (JSONArray) sensorObject.get("Size");
                    Dimension dims = new Dimension(Integer.parseInt(dimensionArray.get(0).toString()), 
                            Integer.parseInt(dimensionArray.get(1).toString()));
                    sensor = new Door(name, location, dims);
                    break;
                case "MotionSensor":
                    if(sensorObject.containsKey("Radius")) sensor = 
                            new MotionSensor(name, location, 
                                    Double.parseDouble(sensorObject.get("Radius").toString()));
                    else{
                        sensor = new MotionSensor(name, location, 
                                Double.parseDouble(sensorObject.get("Direction").toString()), 
                                Double.parseDouble(sensorObject.get("Range").toString()), 
                                Double.parseDouble(sensorObject.get("FieldOfView").toString()));
                    }
            }
            if(sensorObject.containsKey("Exclude")){
                JSONArray excludeArray = (JSONArray) sensorObject.get("Exclude");
                for(Object exclude: excludeArray){
                    JSONObject exclud = (JSONObject) exclude;
                    JSONArray edgeArray = (JSONArray) exclud.get("Edge");
                    Point edgeLocation = new Point(Integer.parseInt(edgeArray.get(0).toString()), 
                        Integer.parseInt(edgeArray.get(1).toString()));
                    String direction = (String) exclud.get("Direction");
                    Point edge = null;
                    Dimension dim = new Dimension(1000, 1000);
                    switch(direction){
                        case ("Northeast"): 
                            edge = new Point(edgeLocation.x, edgeLocation.y - 1000);
                            break;
                        case ("Northwest"): 
                            edge = new Point(edgeLocation.x - 1000, edgeLocation.y - 1000);
                            break;
                        case ("Southeast"): 
                            edge = new Point(edgeLocation.x, edgeLocation.y);
                            break;
                        case ("Southwest"): 
                            edge = new Point(edgeLocation.x - 1000, edgeLocation.y);
                    }
                    Rectangle exluTangle = new Rectangle(edge, dim);
                    sensor.removeArea(new Area(exluTangle));
                }
            }if(sensorObject.containsKey("Confine")){
                JSONArray coords = (JSONArray) sensorObject.get("Confine");
                int[] x = new int[coords.size()];
                int[] y = new int[coords.size()];
                for(int i = 0; i < coords.size(); i++){
                    JSONArray coord = (JSONArray) coords.get(i);
                    x[i] = Integer.parseInt(coord.get(0).toString());
                    y[i] = Integer.parseInt(coord.get(1).toString());
                }
                Polygon p = new Polygon(x, y, coords.size());
                Area excluPoly = new Area(p);
                sensor.confineToArea(excluPoly);
            }
            sensors.add(sensor);
        }
        
        return sensors;
    }
}
