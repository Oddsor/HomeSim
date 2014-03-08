/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator3;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Odd
 */
class Cone {
    private final Point2D origin;
    private final int directionDegrees;
    private final int[] range;
    private final int fieldOfView;
    
    public Cone(Point2D origin, int directionDegrees, int[] range, int fieldOfView){
        this.origin = origin;
        this.directionDegrees = directionDegrees;
        this.range = range;
        if(range.length != 2) try {
            throw new Exception("Range should contain a minimum and maximum size");
        } catch (Exception ex) {
            Logger.getLogger(Cone.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.fieldOfView = fieldOfView;
    }
    
    public Shape getShape(){
        Shape mainArc = new Arc2D.Double(
                new Rectangle2D.Double(origin.getX() - range[1], 
                        origin.getY() - range[1], 
                        range[1] * 2, range[1] * 2), 
                90 - (directionDegrees - fieldOfView / 2), -fieldOfView, Arc2D.PIE);
        Shape subtract = new Arc2D.Double(
                new Rectangle2D.Double(origin.getX() - range[0], 
                        origin.getY() - range[0], 
                        range[0] * 2, range[0] * 2), 
                90 - (directionDegrees - (fieldOfView + 4) / 2), -(fieldOfView + 4), Arc2D.PIE);
        Area cone = new Area(mainArc);
        cone.subtract(new Area(subtract));
        return cone;
    }
}
