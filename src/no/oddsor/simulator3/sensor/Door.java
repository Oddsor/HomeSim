/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3.sensor;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Odd
 */
public class Door implements Sensor{
    private String name;
    private Point location;
    private Dimension dimensions;
    private Area area;

    public Door(String name, Point location, Dimension dimensions){
        this.name = name;
        this.location = location;
        this.area = new Area(new Rectangle(location, dimensions));
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<SensorArea> getSensorAreas() {
        List<SensorArea> areas = new ArrayList<>();
        areas.add(new SensorArea(name, area));
        return areas;
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
