/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import no.oddsor.simulator3.json.SensorReader;
import no.oddsor.simulator3.sensor.Sensor;
import no.oddsor.simulator3.sensor.SensorArea;

/**
 *
 * @author Odd
 */
public class SensorMap extends JPanel{

    Collection<Sensor> sensors;
    
    public SensorMap() {
        try {
            SensorReader reader = new SensorReader("sensors.json");
            sensors = reader.getSensors();
        } catch (Exception ex) {
            Logger.getLogger(SensorMap.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs); //To change body of generated methods, choose Tools | Templates.
        Graphics2D g = (Graphics2D) grphcs;
        for(Sensor sensor: sensors){
            for(SensorArea area: sensor.getSensorAreas()){
                System.out.println("Printing area: " + area.getName());
                g.setColor(Color.BLACK);
                g.draw(area.getArea());
                Color transparentYellow = new Color(255,255,0,50);
                g.setColor(transparentYellow);
                g.fill(area.getArea());
            }
            g.draw(new Rectangle(new Point(322,303 - 1000), new Dimension(1000, 1000)));
        }
    }
    
    public void checkOverlap(int x, int y){
        Point2D p = new Point(x, y);
    }
}
