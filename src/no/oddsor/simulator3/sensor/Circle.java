/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator3.sensor;

import java.awt.Point;

/**
 * A Circle is just a full Cone.
 * @author Odd
 */
class Circle extends Cone{
    
    public Circle(Point origo, double radian){
        super(origo, 0, new double[]{radian}, 360);
    }
}
