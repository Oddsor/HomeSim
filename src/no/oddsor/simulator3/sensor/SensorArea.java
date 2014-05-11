/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3.sensor;

import java.awt.geom.Area;

/**
 *
 * @author Odd
 */
public class SensorArea{

    private String name;
    private Area area;
    private String lastValue;

    public String getName() {
        return name;
    }
    
    public SensorArea(String name, Area area) {
        this.area = area;
        this.name = name;
        this.lastValue = "";
    }
    
    public Area getArea(){
        return area;
    }
    
    public void setLastValue(String val){
        lastValue = val;
    }
    
    public String getLastValue(){
        return lastValue;
    }
}
