/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;

import javax.swing.JPanel;
import no.oddsor.simulator.db_tables.Path;

public class PainterPanel extends JPanel implements MouseListener, MouseMotionListener {
    
    private ArrayList<Point> points = new ArrayList<Point>();
    private Image image;
    private ArrayList<Point> endPoints;
    
    public void clearPoints(){
        points.clear();
        repaint();
    }
    
    public Point getLastPoint() throws Exception{
        return points.get(points.size() - 1);
    }
    
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
    public PainterPanel(String imgFile)
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
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        g.drawImage(image, 0, 0, image.getWidth(this), image.getHeight(this), this);
        
        g.setColor(Color.red);
        if(endPoints != null && !endPoints.isEmpty()){
            System.out.println(endPoints.toString());
            for(int i = 0; i < endPoints.size(); i++){
                g.drawOval(endPoints.get(i).x - 4, endPoints.get(i).y - 4, 8, 8);
            }
        }
        g.setColor(Color.BLACK);
        
        for (int i = 0; i < points.size() - 2; i++)
        {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    

    @Override
    public void mouseDragged(MouseEvent arg0) {
        points.add(arg0.getPoint());
        repaint(); //request Swing to refresh display as soon as it can
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
        xPressed = arg0.getX();
        yPressed = arg0.getY();
        System.out.println("xPressed: "+xPressed+" ,yPressed: "+yPressed);
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
        xReleased = arg0.getX();
        yReleased = arg0.getY();
        System.out.println("xReleased: "+xPressed+" ,yReleased: "+yPressed);
    }

    void paintEndPoints(ArrayList<Path> paths) {
        if(paths != null && !paths.isEmpty()){
            endPoints = new ArrayList<>();
            for(int i = 0; i < paths.size(); i++){
                endPoints.add(new Point(paths.get(i).startX, paths.get(i).startY));
                endPoints.add(new Point(paths.get(i).endX, paths.get(i).endY));
            }
            System.out.println("Repainting");
            repaint();
        }
    }
}