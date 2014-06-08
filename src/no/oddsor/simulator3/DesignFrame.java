
package no.oddsor.simulator3;

import com.almworks.sqlite4java.SQLiteConnection;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import no.oddsor.simulator3.json.TaskReader;
import no.oddsor.simulator3.tables.Node;

/**
 *
 * @author Odd
 */
public class DesignFrame extends JFrame implements ActionListener{
    private NodePainter painter;
    private JLabel mouseLocation;
    private JPanel nodePanel;
    private String folder;
    
    private JCheckBox isStart;
    private Node selectedNode;
    
    private JComboBox chooser;
    private JList lister;
    private Box posepanel;
    
    public DesignFrame(String folder, SQLiteConnection db){
        setTitle("Editor");
        this.folder = folder;
        try{
            painter = new NodePainter(this, folder + "/environment.jpg", db);
        }catch(Exception e){
            e.printStackTrace();
        }
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        
        Box horizontal = Box.createHorizontalBox();
        Box menuBox = Box.createVerticalBox();
        
        mouseLocation = new JLabel("");
        nodePanel = new JPanel();
        nodePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuBox.add(nodePanel);
        JLabel t1 = new JLabel("Right-click: Add node\n");
        menuBox.add(t1);
        menuBox.add(new JLabel("Left-click: Select node"));
        menuBox.add(new JLabel("CTRL+Left-click: Link nodes"));
        menuBox.add(new JLabel("Shift+drag: Move node"));
        menuBox.add(new JLabel("Delete: remove selected node"));
        menuBox.add(new JSeparator());
        menuBox.add(new JLabel("NB! restart simulator for"));
        menuBox.add(new JLabel("changes to take effect!"));
        menuBox.add(new JSeparator());
        menuBox.add(mouseLocation);
        menuBox.setPreferredSize(new Dimension(250, 300));
        
        horizontal.add(menuBox);
        horizontal.add(painter);
        add(horizontal);
        pack();
    }

    void setActiveNode(Node selectedPoint) throws Exception {
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
        TaskReader json = new TaskReader(folder);
        Set<String> objectList = json.getAppliances();
        chooser = new JComboBox(objectList.toArray());
        pan.add(chooser);
        Box buttens = Box.createHorizontalBox();
        JButton adder = new JButton("Add");
        adder.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                selectedNode.addObject(chooser.getSelectedItem().toString());
                try {
                    setActiveNode(selectedNode);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                repaint();
            }
        });
        buttens.add(adder);
        JButton remover = new JButton("Remove");
        remover.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                selectedNode.removeObject(lister.getSelectedValue().toString());
                try {
                    setActiveNode(selectedNode);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                repaint();
            }
        });
        buttens.add(remover);
        pan.add(buttens);
        Iterator it = selectedPoint.types.iterator();
        ArrayList<String> list = new ArrayList<>();
        while(it.hasNext()){
            Appliance obj = (Appliance) it.next();
            list.add(obj.type);
        }
        Object[] nodeObjects = new Object[list.size()];
        for(int i = 0; i < nodeObjects.length; i++){
            nodeObjects[i] = list.get(i);
            System.out.println(list.get(i));
        }
        lister = new JList(nodeObjects);
        lister.setVisibleRowCount(5);
        pan.add(lister);
        posepanel = Box.createVerticalBox();
        pan.add(posepanel);
        lister.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if(!lse.getValueIsAdjusting()){
                    String selected = lister.getSelectedValue().toString();
                    final JTextField poses = new JTextField(7);
                    for(final Appliance app: selectedNode.types){
                        if(app.type.equals(selected)){
                            StringBuffer sb = new StringBuffer();
                            for(String pose: app.getPoses()){
                                sb.append(pose).append(" ");
                            }
                            poses.setText(sb.toString());
                            JButton update = new JButton("Update");
                            
                            update.setPreferredSize(new Dimension(70, 30));
                            update.setAction(new AbstractAction("Update"){
                                
                                @Override
                                public void actionPerformed(ActionEvent ae) {
                                    selectedNode.updatePoses(app, poses.getText());
                                }
                            });
                            posepanel.removeAll();
                            posepanel.add(new JLabel("Poses(space separated)"));
                            posepanel.add(poses);
                            posepanel.add(update);
                            pack();
                            
                        }
                    }
                }
            }
        });
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
