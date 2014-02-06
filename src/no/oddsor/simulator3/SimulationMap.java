
package no.oddsor.simulator3;

import com.almworks.sqlite4java.SQLiteConnection;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Odd
 */
class SimulationMap {
    /**
    * Can move approx 50 pixels in appsketch.jpg-image.
    */
    public final int walkingSpeedPerSec;
    
    public final String mapName;
    public final int startNodeId;
    
    private ArrayList<Node> nodes;
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
    
    public ArrayList<Node> getRouteToNode(Node goal, Point startPoint){
        System.out.println("Trying to find route I think");
        Node start = getClosestNode(startPoint);
        
        Set closedSet = new HashSet();
        Set openSet = new HashSet();
        Map<Node, Node> cameFrom = new HashMap<>();
        openSet.add(start);
        Map<Node, Double> gScore = new HashMap<>();
        gScore.put(start, 0.0);
        Map<Node, Double> fScore = new HashMap<>();
        fScore.put(start, start.distance(goal.getLocation()));
        
        int cunter = 0;
        while(!openSet.isEmpty()){
            cunter++;
            System.out.println("Round " + cunter);
            Node current = getLowest(fScore, openSet);
            if(current.id == goal.id) return reconstructPath(cameFrom, goal);
            openSet.remove(current);
            closedSet.add(current);
            
            Collection<Node> neighbours = current.getNeighbours();
            for(Node neighbour: neighbours){
                if(closedSet.contains(neighbour)){
                    System.out.println("skip");
                    continue;
                }
                double tentativeG = gScore.get(current) + 
                        current.distance(neighbour.getLocation());
                if(!openSet.contains(neighbour) || tentativeG < gScore.get(current)){
                    cameFrom.put(neighbour, current);
                    gScore.put(neighbour, tentativeG);
                    fScore.put(neighbour, tentativeG + 
                            neighbour.distance(goal.getLocation()));
                    openSet.add(neighbour);
                }
            }
        }
        return null;
    }
    
    public ArrayList<Node> getRouteToObject(ArrayList<HouseObject> objects, Point startPoint){
        Node start = getClosestNode(startPoint);
        
        Set closedSet = new HashSet();
        Set openSet = new HashSet();
        Map<Node, Node> cameFrom = new HashMap<>();
        openSet.add(start);
        Map<Node, Double> gScore = new HashMap<>();
        gScore.put(start, 0.0);
        Map<Node, Double> fScore = new HashMap<>();
        fScore.put(start, start.distance(objects.get(0).location.getLocation()));
        
        int cunter = 0;
        while(!openSet.isEmpty()){
            cunter++;
            System.out.println("Round " + cunter);
            Node current = getLowest(fScore, openSet);
            for(HouseObject object: objects){
                if(current.id == object.location.id) return reconstructPath(cameFrom, object.location);
            }
            openSet.remove(current);
            closedSet.add(current);
            
            Collection<Node> neighbours = current.getNeighbours();
            for(Node neighbour: neighbours){
                if(closedSet.contains(neighbour)){
                    System.out.println("skip");
                    continue;
                }
                double tentativeG = gScore.get(current) + 
                        current.distance(neighbour.getLocation());
                if(!openSet.contains(neighbour) || tentativeG < gScore.get(current)){
                    cameFrom.put(neighbour, current);
                    gScore.put(neighbour, tentativeG);
                    fScore.put(neighbour, tentativeG + 
                            neighbour.distance(objects.get(0).location.getLocation()));
                    openSet.add(neighbour);
                }
            }
        }
        return null;
    }
    
    private ArrayList<Node> reconstructPath(Map<Node, Node> cameFrom, Node current) {
        ArrayList<Node> nodes = new ArrayList<>();
        if(cameFrom.containsKey(current)){
            nodes.addAll(reconstructPath(cameFrom, cameFrom.get(current)));
        }
        nodes.add(current);
        return nodes;
    }
    
    private Node getLowest(Map<Node, Double> scores, Set<Node> openSet){
        double shortestDistance = 10000.0;
        Node shortestNode = null;
        for(Node node: scores.keySet()){
            if(scores.get(node) < shortestDistance && openSet.contains(node)){
                shortestNode = node;
                shortestDistance = scores.get(node);
            }
        }
        return shortestNode;
    }

    private Node getClosestNode(Point start) {
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
