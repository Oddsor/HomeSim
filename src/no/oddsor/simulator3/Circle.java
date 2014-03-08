/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator3;

import java.awt.Point;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Odd
 */
class Circle {
    
    private final Point origo;
    private final double radian;
    
    public Circle(Point origo, double radian){
        this.origo = origo;
        this.radian = radian;
    }
    
    public Ellipse2D.Double getEllipse(){
        return new Ellipse2D.Double(origo.x - radian, origo.y - radian, radian * 2, radian * 2);
    }
}
