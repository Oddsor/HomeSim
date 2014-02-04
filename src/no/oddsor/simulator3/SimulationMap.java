
package no.oddsor.simulator3;

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
    
    private Collection<Node> nodes;
    
    public SimulationMap(String mapName, int walkingDistancePerSec){
        this.mapName = mapName;
        this.walkingSpeedPerSec = walkingDistancePerSec;
    }
    
    public Collection<Node> getRouteToNode(Node goal, Point startPoint){
        Node start = getClosestNode(startPoint);
        
        Set closedSet = new HashSet(nodes);
        Set openSet = new HashSet();
        Map<Node, Node> cameFrom = new HashMap<>();
        openSet.add(start);
        Map<Node, Double> gScore = new HashMap<>();
        gScore.put(start, 0.0);
        Map<Node, Double> fScore = new HashMap<>();
        fScore.put(start, start.distance(goal.getLocation()));
        
        
        while(!openSet.isEmpty()){
            Node current = getLowest(fScore);
            if(current.equals(goal)) return reconstructPath(cameFrom, goal);
            openSet.remove(current);
            closedSet.add(current);
            
            Collection<Node> neighbours = current.getNeighbours();
            for(Node neighbour: neighbours){
                if(closedSet.contains(neighbour)){
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
    
    private Collection<Node> reconstructPath(Map<Node, Node> cameFrom, Node current) {
        Collection<Node> nodes = new ArrayList<>();
        if(cameFrom.containsKey(current)){
            nodes.addAll(reconstructPath(cameFrom, cameFrom.get(current)));
        }
        nodes.add(current);
        return nodes;
    }
    
    private Node getLowest(Map<Node, Double> scores){
        double shortestDistance = 10000.0;
        Node shortestNode = null;
        for(Node node: scores.keySet()){
            if(scores.get(node) < shortestDistance){
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
