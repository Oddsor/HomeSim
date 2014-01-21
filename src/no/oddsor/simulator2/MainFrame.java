/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator2;

import com.almworks.sqlite4java.SQLiteConnection;
import java.awt.GridLayout;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

/**
 *
 * @author Odd
 */
public class MainFrame extends JFrame{
    private SimulationDisplay painter;
    private DatabaseHandler dbHandler;
    private SQLiteConnection db;
    private LayoutManager2 layout;
    
    private DesignFrame designer;
    
    public HashMap<Integer, Object> options;
    
    public MainFrame(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("PersonSimulator");
        
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                db.dispose();
            }
        });

        try{
            dbHandler = new DatabaseHandler();
            db = dbHandler.getDb();
            db.open();
            painter = new SimulationDisplay(new Simulation(true, dbHandler), 
                    "appsketch.jpg", new Point(), db);
            designer = new DesignFrame(this, db);
        }catch(Exception e){
            
        }
        
        Box box = Box.createHorizontalBox();
        Box menubox = Box.createVerticalBox();
        
        ArrayList<String> demo = new ArrayList<>();
        demo.add("Hunger");
        demo.add("Fun");
        demo.add("Energy");
        
        JPanel needsPanel = new JPanel(new GridLayout(demo.size(), 2));
        for(String str: demo){
            needsPanel.add(new JLabel(str));
            needsPanel.add(new JProgressBar(0, 100));
        }
        needsPanel.setMaximumSize(needsPanel.getPreferredSize());
        menubox.add(needsPanel);
        menubox.add(new JPanel());
        Box bottomButtonBox = Box.createHorizontalBox();
        menubox.add(bottomButtonBox);
        
        JButton editButton = new JButton("Open editor");
        editButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                designer.setVisible(true);
            }
        });
        bottomButtonBox.add(new JButton("Start sim"));
        bottomButtonBox.add(editButton);
        box.add(painter);
        box.add(menubox);
        add(box);
        pack();
    }
    
    public static void main(String[] args){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}


