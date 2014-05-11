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

import java.awt.Point;
import java.awt.geom.Area;
import java.util.List;

/**
 *
 * @author Odd
 */
public interface Sensor {
    
    String getName();
    
    List<SensorArea> getSensorAreas();
    
    void removeArea(Area area);
    
    void confineToArea(Area area);
    
    Point getPosition();
}
