/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 *
 * @author Odd
 */
public class SensorMap extends JPanel{

    private Cone c;
    private Cone c2;
    private Cone c3;
    
    public SensorMap() {
        c = new Cone(new Point(200, 200), 90, new int[]{50, 100}, 20);
        c2 = new Cone(new Point(200, 200), 0, new int[]{100, 150}, 60);
        c3 = new Cone(new Point(200, 200), 270, new int[]{50, 100}, 20);
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs); //To change body of generated methods, choose Tools | Templates.
        Graphics2D g = (Graphics2D) grphcs;
        g.setColor(Color.BLACK);
        g.fill(c.getShape());
        g.setColor(Color.BLUE);
        g.fill(c2.getShape());
        g.setColor(Color.ORANGE);
        g.fill(c3.getShape());
        g.fill(new Rectangle2D.Double(100, 100, 20, 20));
    }
    
    public void checkOverlap(int x, int y){
        Point2D p = new Point(x, y);
        System.out.println(c.getShape().contains(p));
        System.out.println(c2.getShape().contains(p));
        System.out.println(c3.getShape().contains(p));
    }
}
