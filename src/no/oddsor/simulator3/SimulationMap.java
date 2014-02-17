
package no.oddsor.simulator3;

import com.almworks.sqlite4java.SQLiteConnection;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Odd
 */
public class SimulationMap {
    /**
    * Can move approx 50 pixels in appsketch.jpg-image.
    */
    public final int walkingSpeedPerSec;
    
    public final String mapName;
    public final int startNodeId;
    
    private final ArrayList<Node> nodes;
    private final Collection<Person> people;
    public ArrayList<HouseObject> objects;
    
    public SimulationMap(String mapName, int walkingDistancePerSec, int startId, 
            Collection<Person> people, SQLiteConnection db){
        this.mapName = mapName;
        this.people = people;
        this.walkingSpeedPerSec = walkingDistancePerSec;
        this.startNodeId = startId;
        this.nodes = Node.getNodes(db);
        objects = new ArrayList<>();
        for(Node node: nodes){
            if(!node.types.isEmpty()){
                for(HouseObject obj: node.types){
                    objects.add(obj);
                }
            }
        }
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
}