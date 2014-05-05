/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3.sensor;

import java.awt.Point;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;


public class Contact implements Sensor{
    private String name;
    private String attached;
    private List<SensorArea> sensorAreas;
    
    public Contact(String name, String attached){
        this.name = name;
        this.attached = attached;
        sensorAreas = new ArrayList<>(1);
        sensorAreas.add(new SensorArea(name, null));
    }
    
    public String getAttached(){
        return attached;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<SensorArea> getSensorAreas() {
        return sensorAreas;
    }

    @Override
    public void removeArea(Area area) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void confineToArea(Area area) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Point getPosition() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
