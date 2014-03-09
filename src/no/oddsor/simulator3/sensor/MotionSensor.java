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

/**
 *
 * @author Odd
 */
public class MotionSensor implements Sensor{
    
    private String name;
    private Point location;
    private Area area;

    public MotionSensor(String name, Point location, double radian) {
        this.name = name;
        this.location = location;
        this.area = new Area(new Circle(location, radian).getShape());
    }

    public MotionSensor(String name, Point location, double directionDegrees, double range, double fieldOfView) {
        this.name = name;
        this.area = new Area(new Cone(location, directionDegrees, range, fieldOfView).getShape());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<SensorArea> getSensorAreas() {
        ArrayList<SensorArea> l = new ArrayList<>();
        l.add(new SensorArea(name, area));
        
        return l;
    }

    @Override
    public void removeArea(Area area) {
        this.area.subtract(area);
    }

    @Override
    public Point getPosition() {
        return location;
    }
    
}
