
package no.oddsor.simulator3;

import no.oddsor.simulator3.tables.Node;
import com.almworks.sqlite4java.SQLiteConnection;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.oddsor.simulator3.json.SensorReader;
import no.oddsor.simulator3.sensor.Sensor;


public class SimulationMap {
    /**
    * Can move approx 50 pixels in appsketch.jpg-image.
    */
    public final int walkingSpeedPerSec;
    
    public final String mapName;
    public final int startNodeId;
    public final int dotsPerMeter;
    
    private ArrayList<Node> nodes;
    private final Collection<Person> people;
    public ArrayList<Appliance> objects;
    public Collection<Item> items;
    private Collection<Sensor> sensors;

    public Collection<Sensor> getSensors() {
        return sensors;
    }
    
    public SimulationMap(String mapName, int walkingDistancePerSec, int startId, 
            Collection<Person> people, int dotsPerMeter, SQLiteConnection db){
        this.mapName = mapName;
        this.people = people;
        try {
            sensors = new SensorReader("sensors.json").getSensors();
        } catch (Exception ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.walkingSpeedPerSec = walkingDistancePerSec;
        this.dotsPerMeter = dotsPerMeter;
        this.startNodeId = startId;
        if(db != null) this.nodes = Node.getNodes(db);
        else nodes = new ArrayList<>();
        objects = new ArrayList<>();
        for(Node node: nodes){
            if(!node.types.isEmpty()){
                for(Appliance obj: node.types){
                    objects.add(obj);
                }
            }
        }
        this.items = new ArrayList<>();
    }

    public Node getClosestNode(Point start) {
        double smallestDistance = 100000.0;
        Node smallestNode = null;
        for(Node node: nodes){
            double dist = node.distance(start);
            if(dist < smallestDistance){
                smallestDistance = dist;
                smallestNode = node;
            }
        }
        return smallestNode;
    }
    
    public Collection<Person> getPeople(){
        return people;
    }
    
    public Point moveActor(Person person, int simulationsPerSec){
        int walkingSpeed = (int) (walkingSpeedPerSec / simulationsPerSec);
        Point p = new Point(person.currentLocation());
        Node targetNode = person.getRoute().peek();
        Point targetLocation = targetNode.getLocation();
        double distance = p.distance(targetLocation);
        int dx = targetLocation.x - p.x;
        int dy = targetLocation.y - p.y;
        if(distance < walkingSpeed) p.setLocation(targetLocation);
        else{
            p.translate((int) (walkingSpeed * (dx / distance)), 
                    (int) (walkingSpeed * (dy / distance)));
        }
        return p;
    }
    
    public void addItem(Item item){
        System.out.println("Added " + item.name + " to map");
        this.items.add(item);
    }
    
    public Item popItem(String itemName, Node location){
        for(Item item: items){
            if(location.id == item.location.id && item.name.equals(itemName)){
                items.remove(item);
                return item;
            }
        }
        return null;
    }
    
    public boolean hasItem(String requestedItem, int amount){
        int count = 0;
        for(Item item: items){
            if(item.name.equals(requestedItem)) count++;
        }
        return count >= amount;
    }
    
    public Collection<Node> getLocationsOfItem(String itemName){
        Collection<Node> itemNodes = new ArrayList<>();
        for(Item item: items){
            if(item.name.equals(itemName)){
                itemNodes.add(item.location);
            }
        }
        return itemNodes;
    }
    
    public Collection<Node> getLocationAppliances(Collection<String> appliance){
        Collection<Node> locations = new ArrayList<>();
        for(Appliance app: objects){
            for(String appType: appliance){
                if(app.type.equals(appType)) locations.add(app.location);
            }
            
        }
        return locations;
    }
    
    public Collection<Appliance> getAppliances(){
        return objects;
    }
    
    public Node getRandomNode(){
        List<Node> nodePool = new ArrayList<>(nodes);
        for(Appliance app: objects){
            nodePool.remove(app.location);
        }
        Random rand = new Random();
        return nodePool.get(rand.nextInt(nodePool.size()));
    }
    
    public static void main(String[] args){
        SimulationMap map = new  SimulationMap("", 5, 1, null, 43, null);
        map.addItem(new Item("Wares", null));
        Task t = new Task("ye", "s", 1, null, "ye");
        t.addRequiredItem("Wares", 1);
        System.out.println(t.itemsExist(new Person("s", "oddsurcut.png", null, null), map));
        System.out.println(map.hasItem("Wares", 1));
        System.out.println(map.hasItem("Wares", 2));
    }
}