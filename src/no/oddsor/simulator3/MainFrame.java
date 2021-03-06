
package no.oddsor.simulator3;

import com.almworks.sqlite4java.SQLiteConnection;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import no.oddsor.simulator3.json.SensorReader;
import no.oddsor.simulator3.json.TaskReader;
import no.oddsor.simulator3.sensor.Sensor;


public class MainFrame extends JFrame{
    private SimulationDisplay painter;
    private DatabaseHandler dbHandler;
    private SQLiteConnection db;
    private Box allNeeds;
    private JLabel timeLabel;
    Timer tim;
    Simulator sim;
    SimulationMap map;
    long startTime;
    
    private HashMap<String, JProgressBar> needBars;
    
    private DesignFrame designer;
    
    public HashMap<Integer, Object> options;
    private final JLabel stateList;
    private int days;
    private String folder;
    
    public MainFrame(String folder){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("PersonSimulator");
        
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                db.dispose();
            }
        });

        sim = null;
        this.folder = folder;
        needBars = new HashMap<>();
        try{
            dbHandler = new DatabaseHandler(folder);
            db = dbHandler.getDb();
            db.open();
            ArrayList<Person> people = new ArrayList<>();
            TaskReader j = new TaskReader(folder);
            Collection<Sensor> sr = new SensorReader(folder + "/sensors.json").getSensors();
            map = new SimulationMap(folder + "/environment.jpg", 50, 2, people, 43, sr, db);
            people.add(new Person("Odd", "oddsurcut.png", map.getStartingPoint(), j.getNeeds()));
            sim = new Simulator(map, new TaskManager(j), 3);
            painter = new SimulationDisplay(folder + "/environment.jpg");
            this.days = 30;
            designer = new DesignFrame(folder, db);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        Box box = Box.createHorizontalBox();
        Box menubox = Box.createVerticalBox();
        menubox.setAlignmentY(TOP_ALIGNMENT);
        
        allNeeds = Box.createVerticalBox();
        Collection<Person> allPeople = map.getPeople();
        for(Person person: allPeople){
            List<Need> personNeeds = person.getNeeds();
            JPanel needsPanel = new JPanel(new GridLayout(person.getNeeds().size() + 1, 2));
            needsPanel.add(new JLabel("Person:"));
            needsPanel.add(new JLabel(person.name));
            for(Need need: personNeeds){
                needsPanel.add(new JLabel(need.name()));
                JProgressBar prog = new JProgressBar(0, 100);
                prog.setName(person.name + "," + need.name());
                prog.setValue((int) need.getValue());
                prog.setString("" + (int) need.getValue());
                prog.setStringPainted(true);
                needBars.put(prog.getName(), prog);
                needsPanel.add(prog);
                needsPanel.setMaximumSize(needsPanel.getPreferredSize());
                allNeeds.add(needsPanel);
            }
        }
        menubox.add(allNeeds);
        timeLabel = new JLabel("Time");
        menubox.add(timeLabel);
        Box bottomButtonBox = Box.createVerticalBox();
        menubox.add(bottomButtonBox);
        JPanel durationP = new JPanel();
        durationP.add(new JLabel("Duration in days: "));
        final JTextField daysField = new JTextField("30", 10);
        durationP.add(daysField);
        menubox.add(durationP);
        JButton editButton = new JButton("Open editor");
        editButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                designer.setVisible(true);
            }
        });
        bottomButtonBox.add(editButton);
        menubox.add(new JPanel());
        box.add(painter);
        painter.update(map.getPeople(), map.getSensors());
        box.add(menubox);
        add(box);
        final JTextField filenameField = new JTextField("sensorvals");
        ActionListener timeListen = new ActionListener() {
            int slowSpeed = 10;
            int fastSpeed = 1;

            @Override
            public void actionPerformed(ActionEvent ae) {
                if(!sim.simulationStep()) tim.setDelay(fastSpeed);
                else{
                    tim.setDelay(slowSpeed);
                    painter.update(map.getPeople(), map.getSensors());
                    updateMenu();
                }
                if(Time.getDay(sim.currentTime) == days + 1){
                    tim.stop();
                    System.out.println("Elapsed time in milliseconds: " + (System.currentTimeMillis() - startTime));
                }
            }
        };
        tim = new Timer(10, timeListen);
        JButton startStop = new JButton("Start");
        startStop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    sim.setSensorLogger(new SensorLogger(filenameField.getText()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                days = Integer.parseInt(daysField.getText());
                startTime = System.currentTimeMillis();
                tim.start();
            }
        });
        stateList = new JLabel("States: ");
        menubox.add(stateList);
        JPanel container = new JPanel();
        filenameField.setPreferredSize(new Dimension(150, 30));
        container.add(new JLabel("Dataset filename:"));
        container.add(filenameField);
        menubox.add(container);
        menubox.add(startStop);
        pack();
    }
    
    public void updateMenu(){
        Collection<Person> people = map.getPeople();
        String info = "";
        for(Person person: people){
            List<Need> needs = person.getNeeds();
            for(Need need: needs){
                JProgressBar p = needBars.get(person.name+","+need.name());
                p.setValue((int) need.getValue());
                p.setString("" + (int) need.getValue());
            }info += person.name + person.getState() + "\n";
        }
        stateList.setText(info);
        timeLabel.setText("W: " + sim.time.getWeek(sim.currentTime) + 
                ", D: " + sim.time.getDayName(sim.currentTime) + 
                ", " + sim.time.getNumberFormatted(sim.time.getHours(sim.currentTime)) + 
                ":" + sim.time.getNumberFormatted(sim.time.getMinutes(sim.currentTime)) +
                ":" + sim.time.getNumberFormatted(sim.time.getSeconds(sim.currentTime)));
    }
    
    public static void main(String[] args){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                new MainFrame("home1").setVisible(true);
            }
        });
    }
}


