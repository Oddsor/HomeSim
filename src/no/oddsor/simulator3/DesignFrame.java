
package no.oddsor.simulator3;

import no.oddsor.simulator3.enums.ObjectTypes;
import com.almworks.sqlite4java.SQLiteConnection;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

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
    
    private JComboBox chooser;
    private JList lister;
    
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
        menuBox.setPreferredSize(new Dimension(200, 300));
        
        horizontal.add(menuBox);
        horizontal.add(painter);
        add(horizontal);
        pack();
    }

    void setActiveNode(Node selectedPoint) {
        System.out.println("Active: " + selectedPoint.id);
        nodePanel.removeAll();
        Box pan = Box.createVerticalBox();
        isStart = new JCheckBox("Starting point");
        isStart.addActionListener(this);
        int start = 0;
        
        if(selectedPoint.id == start){
            isStart.setSelected(true);
            isStart.setEnabled(false);
        }
        selectedNode = selectedPoint;
        pan.add(isStart);
        
        Object[] objectList = ObjectTypes.values();
        chooser = new JComboBox(objectList);
        pan.add(chooser);
        Box buttens = Box.createHorizontalBox();
        JButton adder = new JButton("Add");
        adder.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                selectedNode.addObject(ObjectTypes.valueOf(chooser.getSelectedItem().toString()));
                setActiveNode(selectedNode);
            }
        });
        buttens.add(adder);
        JButton remover = new JButton("Remove");
        remover.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                selectedNode.removeObject(ObjectTypes.valueOf(lister.getSelectedValue().toString()));
                setActiveNode(selectedNode);
            }
        });
        buttens.add(remover);
        pan.add(buttens);
        Iterator it = selectedPoint.types.iterator();
        ArrayList<String> list = new ArrayList<>();
        while(it.hasNext()){
            HouseObject obj = (HouseObject) it.next();
            list.add(obj.type.name());
        }
        Object[] nodeObjects = new Object[list.size()];
        for(int i = 0; i < nodeObjects.length; i++){
            nodeObjects[i] = list.get(i);
            System.out.println(list.get(i));
        }
        lister = new JList(nodeObjects);
        lister.setVisibleRowCount(5);
        pan.add(lister);
        nodePanel.add(pan);
        pack();
    }
    
    void setMouseLocation(Point location){
        mouseLocation.setText(location.x + ", " + location.y);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        /*if(ae.getSource().equals(isStart)){
            mainFrame.options.remove(Options.START_LOCATION);
            mainFrame.options.put(Options.START_LOCATION, selectedNode);
        }*/
    }
}
