/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator2;

import com.almworks.sqlite4java.SQLiteConnection;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import no.oddsor.simulator2.db_tables.Node;

/**
 *
 * @author Odd
 */
public class DesignFrame extends JFrame{
    private DatabaseHandler dbHandler;
    private SQLiteConnection db;
    private NodePainter painter;
    
    public DesignFrame(SQLiteConnection db){
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
        horizontal.add(menuBox);
        horizontal.add(painter);
        add(horizontal);
        pack();
    }

    void setActiveNode(Node selectedPoint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
