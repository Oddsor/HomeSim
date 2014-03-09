/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator3.sensor;

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
    private final double directionDegrees;
    private final double[] range;
    private final double fieldOfView;
    
    public Cone(Point2D origin, double directionDegrees, double [] range, double fieldOfView){
        this.origin = origin;
        this.directionDegrees = directionDegrees;
        this.range = range;
        if(range.length > 2 || range.length < 1) try {
            throw new Exception("Range should not have more than two values or less than one");
        } catch (Exception ex) {
            Logger.getLogger(Cone.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.fieldOfView = fieldOfView;
    }
    
    public Cone(Point2D origin, double directionDegrees, double range, double fieldOfView){
        this.origin = origin;
        this.directionDegrees = directionDegrees;
        this.range = new double[]{range};
        this.fieldOfView = fieldOfView;
    }
    
    public Shape getShape(){
        Shape mainArc = new Arc2D.Double(
                new Rectangle2D.Double(origin.getX() - range[range.length - 1], 
                        origin.getY() - range[range.length - 1], 
                        range[range.length - 1] * 2, range[range.length - 1] * 2), 
                90 - (directionDegrees - fieldOfView / 2), -fieldOfView, Arc2D.PIE);
        Area cone = new Area(mainArc);
        if(range.length == 2){
            Shape subtract = new Arc2D.Double(
                new Rectangle2D.Double(origin.getX() - range[0], 
                        origin.getY() - range[0], 
                        range[0] * 2, range[0] * 2), 
                90 - (directionDegrees - (fieldOfView + 4) / 2), -(fieldOfView + 4), Arc2D.PIE);
        
            cone.subtract(new Area(subtract));
        }
        return cone;
    }
}
