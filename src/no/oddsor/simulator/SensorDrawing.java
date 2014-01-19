
package no.oddsor.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;

import javax.swing.JPanel;

public class SensorDrawing extends JPanel implements MouseListener, MouseMotionListener {
    
    private ArrayList<Circle> points = new ArrayList<Circle>();
    
    int mode = 0;
    
    private Image image;
    
    public Dimension getDimensions(){
        return new Dimension(image.getWidth(this), image.getHeight(this));
    }
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    int xPressed,yPressed;
    int xReleased,yReleased;
    int xDragged,yDragged;
    public SensorDrawing(String imgFile, ArrayList<Circle> circles, ArrayList<Cone> cones)
    {
        image = new ImageIcon(getClass().getResource(imgFile)).getImage();
        setSize(new Dimension(image.getWidth(this), image.getHeight(this)));
        
        setBounds(0, 0, image.getWidth(this), image.getHeight(this));
        setOpaque(false);
        addMouseListener(this);
        addMouseMotionListener(this);
        
        System.out.println("Added paintypanel");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, image.getWidth(this), image.getHeight(this), this);

        for(Circle circle :points){
            g.fillOval(circle.xPosition, circle.yPosition, circle.width, circle.height);
        }
        if (mode == 1 || mode == 2) g.drawLine(xPressed, yPressed, xReleased, yReleased);
        if(mode == 2) {
            int[] xes = new int[3];
            int[] yes = new int[3];
            xes[0] = xPressed;
            yes[0] = yPressed;
            xes[1] = xReleased;
            yes[1] = yReleased;
            xes[2] = xDragged;
            yes[2] = yDragged;
            g.drawPolygon(new Polygon(xes, yes, 3));
        }
/*        int xpos = 0;
        int ypos = 0;
        if(xReleased > xPressed) xpos = xPressed - (xReleased - xPressed);
        else xpos = xReleased;
        if(yReleased > yPressed) ypos = yPressed - (yReleased - yPressed);
        else ypos = yReleased;
        
        g.drawOval(xpos, ypos, (xPressed - xpos)*2, (yPressed - ypos)*2);
        g.fillOval(xpos, ypos, (xPressed - xpos)*2, (yPressed - ypos)*2);*/
    }

    

    @Override
    public void mouseDragged(MouseEvent arg0) {
        /*xReleased = arg0.getX();
        yReleased = arg0.getY();
        repaint(); */
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        xReleased = arg0.getX();
        yReleased = arg0.getY();
        repaint(); //request Swing to refresh display as soon as it can
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {

    }

    @Override
    public void mouseExited(MouseEvent arg0) {

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        if(mode == 0){
            mode = 1;
            xPressed = arg0.getX();
            yPressed = arg0.getY();
        }else if(mode == 1){
            mode = 2;
            xDragged = arg0.getX();
            yDragged = arg0.getY();
        }else if(mode == 2) mode = 0;
        /*xPressed = arg0.getX();
        yPressed = arg0.getY();
        System.out.println("xPressed: "+xPressed+" ,yPressed: "+yPressed);*/
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        /*xReleased = arg0.getX();
        yReleased = arg0.getY();
        int xpos = 0;
        int ypos = 0;
        if(xReleased > xPressed) xpos = xPressed - (xReleased - xPressed);
        else xpos = xReleased;
        if(yReleased > yPressed) ypos = yPressed - (yReleased - yPressed);
        else ypos = yReleased;
        points.add(new Circle(xpos, ypos, (xPressed - xpos)*2, (yPressed - ypos)*2));*/
        
    }
}