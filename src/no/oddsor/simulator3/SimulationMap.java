
package no.oddsor.simulator3;

import com.almworks.sqlite4java.SQLiteConnection;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * An implementation of a map. Players move on an screencap of an apartment's
 * layout via a node network.
 * @author Odd
 */
public class SimulationMap {
    /**
    * Can move approx 50 pixels in appsketch.jpg-image.
    */
    public final int walkingSpeedPerSec;
    
    public final String mapName;
    public final int startNodeId;
    
    private ArrayList<Node> nodes;
    private final Collection<Person> people;
    public ArrayList<HouseObject> objects;
    public Collection<Item> items;
    
    public SimulationMap(String mapName, int walkingDistancePerSec, int startId, 
            Collection<Person> people, SQLiteConnection db){
        this.mapName = mapName;
        this.people = people;
        this.walkingSpeedPerSec = walkingDistancePerSec;
        this.startNodeId = startId;
        if(db != null) this.nodes = Node.getNodes(db);
        else nodes = new ArrayList<>();
        objects = new ArrayList<>();
        for(Node node: nodes){
            if(!node.types.isEmpty()){
                for(HouseObject obj: node.types){
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
    
    public Collection<Node> getLocationAppliances(List<String> appliance){
        Collection<Node> locations = new ArrayList<>();
        for(HouseObject app: objects){
            for(String appType: appliance){
                if(app.type.equals(appType)) locations.add(app.location);
            }
            
        }
        return locations;
    }
    
    public Collection<HouseObject> getAppliances(){
        return objects;
    }
    
    public Node getRandomNode(){
        List<Node> nodePool = new ArrayList<>(nodes);
        for(HouseObject app: objects){
            nodePool.remove(app.location);
        }
        Random rand = new Random();
        return nodePool.get(rand.nextInt(nodePool.size()));
    }
    
    public static void main(String[] args){
        SimulationMap map = new  SimulationMap("", 5, 1, null, null);
        map.addItem(new Item("Wares", null));
        Task t = new Task("ye", "s", 1, null);
        t.addRequiredItem("Wares", 1);
        System.out.println(t.itemsExist(new Person("s", "oddsurcut.png", null, null), map));
        System.out.println(map.hasItem("Wares", 1));
        System.out.println(map.hasItem("Wares", 2));
    }
}