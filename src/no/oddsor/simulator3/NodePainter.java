
package no.oddsor.simulator3;

import com.almworks.sqlite4java.SQLiteConnection;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

import javax.swing.JPanel;

public class NodePainter extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

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
    
    boolean draggingPoint = false;
    boolean drawingLine = false;
    
    Node selectedPoint;
    Node hoveredPoint;
    DesignFrame simFrame;
    
    //private DatabaseHandler dbHandler;
    private SQLiteConnection db;
    private ArrayList<Node> points;
    private Image image;
    ArrayList<Edge> edges;
    
    public NodePainter(DesignFrame frame, String imgFile, SQLiteConnection db)
    {
        this.simFrame = frame;
        this.db = db;
        image = new ImageIcon(getClass().getResource(imgFile)).getImage();
        setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
        
        setBounds(0, 0, image.getWidth(this), image.getHeight(this));
        setOpaque(false);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        
        points = Node.getNodes(db);
        System.out.println(points.size());
        System.out.println(getPreferredSize());
        edges = Edge.getEdges(points, db);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        g.drawImage(image, 0, 0, image.getWidth(this), image.getHeight(this), this);
        
        g.setColor(Color.red);
        if(points != null && !points.isEmpty()){
            for(int i = 0; i < points.size(); i++){
                g.fillOval(points.get(i).getLocation().x - 4, points.get(i).getLocation().y - 4, 8, 8);
            }
        }
        
        g.setColor(Color.yellow);
        if(hoveredPoint != null) g.fillOval(hoveredPoint.getLocation().x - 4, hoveredPoint.getLocation().y - 4, 8, 8);
        
        g.setColor(Color.red);
        if(selectedPoint != null) g.fillOval(selectedPoint.getLocation().x - 6, selectedPoint.getLocation().y - 6, 12, 12);
        g.setColor(Color.yellow);
        if(selectedPoint != null) g.fillOval(selectedPoint.getLocation().x - 4, selectedPoint.getLocation().y - 4, 8, 8);
        
        
        g.setColor(Color.BLACK);
        
        for (int i = 0; i < edges.size(); i++)
        {
            g.drawLine(edges.get(i).a.getLocation().x, edges.get(i).a.getLocation().y, edges.get(i).b.getLocation().x, edges.get(i).b.getLocation().y);
        }
        if(drawingLine) g.drawLine(selectedPoint.getLocation().x, selectedPoint.getLocation().y, xDragged, yDragged);
    }

    

    @Override
    public void mouseDragged(MouseEvent arg0) {
        if(draggingPoint && hoveredPoint != null){
            hoveredPoint.setLocation(arg0.getX(), arg0.getY());
        }
        repaint(); //request Swing to refresh display as soon as it can
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        if(hoveredPoint != null){
            if((arg0.getX() < hoveredPoint.getLocation().x - 4 || arg0.getX() > hoveredPoint.getLocation().x + 4) ||
                        (arg0.getY() < hoveredPoint.getLocation().y - 4 || arg0.getY() > hoveredPoint.getLocation().y + 4)){
                    hoveredPoint = null;
                    repaint();
                }
        }else{
            for(Node point: points){
                if((arg0.getX() > point.getLocation().x - 4 && arg0.getX() < point.getLocation().x + 4) &&
                        (arg0.getY() > point.getLocation().y - 4 && arg0.getY() < point.getLocation().y + 4)){
                    hoveredPoint = point;
                    repaint();
                }
            }
        }
        if(drawingLine){
            xDragged = arg0.getX();
            yDragged = arg0.getY();
            repaint();
        }
        simFrame.setMouseLocation(new Point(arg0.getX(), arg0.getY()));
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        
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
        
        if(draggingPoint && hoveredPoint != null){
            hoveredPoint.setLocation(arg0.getX(), arg0.getY());
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
        xReleased = arg0.getX();
        yReleased = arg0.getY();
        addKeyListener(this);
        
        //if(draggingPoint == true) draggingPoint = false;
        if(selectedPoint != null && arg0.getButton() == MouseEvent.BUTTON1 && drawingLine){
            for(Node point: points){
                if((arg0.getX() > point.getLocation().x - 4 && arg0.getX() < point.getLocation().x + 4) &&
                            (arg0.getY() > point.getLocation().y - 4 && arg0.getY() < point.getLocation().y + 4)){
                    Edge edg = new Edge(-1, point, selectedPoint);
                    if(!edg.exists(edges, point, selectedPoint)){
                        edg.update(db);
                        edges.add(edg);
                        repaint();
                        break;
                    }
                    else{
                        System.out.println("Edge exists!");
                        break;
                    }
                }
            }
        }else if(hoveredPoint != null){
            selectedPoint = hoveredPoint;
            try {
                simFrame.setActiveNode(selectedPoint);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else if(hoveredPoint != null && draggingPoint){
            hoveredPoint.update();
        }else if(arg0.getButton() == MouseEvent.BUTTON3 && selectedPoint != null){
            selectedPoint = null;
        }
        else if(arg0.getButton() == MouseEvent.BUTTON3 && selectedPoint == null){
            try{
                Node nod = new Node(db, -1, new Point(arg0.getX(), arg0.getY()), null);
                nod.update();
                points.add(nod);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if(ke.getKeyCode() == KeyEvent.VK_SHIFT){
            draggingPoint = true;
        }if(selectedPoint != null && ke.getKeyCode() == KeyEvent.VK_CONTROL){
            drawingLine = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        if(KeyEvent.VK_SHIFT == ke.getKeyCode()){
            draggingPoint = false;
            if(hoveredPoint != null){
                hoveredPoint.update();
            }
        }else if(KeyEvent.VK_CONTROL == ke.getKeyCode()){
            drawingLine = false;
            repaint();
        }
        else if(selectedPoint != null && ke.getKeyCode() == KeyEvent.VK_DELETE){
            for(int i = 0; i < points.size(); i++){
                if(points.get(i).equals(selectedPoint)){
                    points.remove(i);
                    for(int j = 0; j < edges.size(); j++){
                        if(edges.get(j).a.equals(selectedPoint) || 
                                edges.get(j).b.equals(selectedPoint)){
                            edges.get(j).removeEdge(db);
                            edges.remove(j);
                            if(!edges.isEmpty()) j = -1;
                        }
                    }
                    selectedPoint.remove();
                    selectedPoint = null;
                    repaint();
                    break;
                }
            }
        }
    }
}