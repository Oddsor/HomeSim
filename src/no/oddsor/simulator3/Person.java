
package no.oddsor.simulator3;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Odd
 */
class Person {

    private Point currentLocation;
    private ArrayList<Node> route;
    private ArrayList<Need> needs;
    private Task currentTask;
    
    private Set inventory;
    
    Point currentLocation() {
        return currentLocation;
    }

    ArrayList<Node> getRoute() {
        if(route == null || route.isEmpty()) return null;
        return route;
    }

    void setLocation(Point moveToPoint) {
        currentLocation = moveToPoint;
        if((route != null || !route.isEmpty()) && 
                currentLocation.distance(route.get(0).getLocation()) == 0){
            route.remove(0);
        }
    }
    
    public ArrayList<Need> getSortedNeeds(){
        return needs;
    }

    void setTask(Task nextTask, SimulationMap map) {
        currentTask = nextTask;
        route = new ArrayList<>(map.getRouteToNode(nextTask.getNode(), currentLocation));
    }

    void passTime(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
