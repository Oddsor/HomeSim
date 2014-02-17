
package no.oddsor.simulator3;

import com.almworks.sqlite4java.SQLiteConnection;
import java.awt.Point;
import java.util.ArrayList;

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
    public ArrayList<HouseObject> objects;
    
    public SimulationMap(String mapName, int walkingDistancePerSec, int startId, SQLiteConnection db){
        this.mapName = mapName;
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
}