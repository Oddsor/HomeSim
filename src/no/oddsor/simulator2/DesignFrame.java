/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator2;

import no.oddsor.simulator2.db_tables.Options;
import com.almworks.sqlite4java.SQLiteConnection;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import no.oddsor.simulator2.db_tables.Node;

/**
 *
 * @author Odd
 */
public class DesignFrame extends JFrame implements ActionListener{
    private DatabaseHandler dbHandler;
    private SQLiteConnection db;
    private NodePainter painter;
    private JLabel mouseLocation;
    private JPanel nodePanel;
    private MainFrame mainFrame;
    
    private JCheckBox isStart;
    private Node selectedNode;
    
    public DesignFrame(MainFrame frame, SQLiteConnection db){
        this.mainFrame = frame;
        this.db = db;
        setTitle("Editor");
        try{
            painter = new NodePainter(this, "appsketch.jpg", db);
        }catch(Exception e){
            e.printStackTrace();
        }
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        
        Box horizontal = Box.createHorizontalBox();
        Box menuBox = Box.createVerticalBox();
        
        mouseLocation = new JLabel("");
        nodePanel = new JPanel();
        menuBox.add(nodePanel);
        menuBox.add(mouseLocation);
        
        
        horizontal.add(menuBox);
        horizontal.add(painter);
        add(horizontal);
        pack();
    }

    void setActiveNode(Node selectedPoint) {
        nodePanel = new JPanel();
        isStart = new JCheckBox("Starting point");
        isStart.addActionListener(this);
        Node start = (Node) mainFrame.options.get(Options.START_LOCATION);
        
        
        
        if(selectedPoint.id == start.id){
            isStart.setSelected(true);
            isStart.setEnabled(false);
        }
        selectedNode = selectedPoint;
        nodePanel.add(isStart);
    }
    
    void setMouseLocation(Point location){
        mouseLocation.setText(location.x + ", " + location.y);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource().equals(isStart)){
            mainFrame.options.remove(Options.START_LOCATION);
            mainFrame.options.put(Options.START_LOCATION, selectedNode);
        }
    }
}
