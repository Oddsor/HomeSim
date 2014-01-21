/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator2;

import java.awt.Point;
import java.util.HashMap;
import no.oddsor.simulator2.db_tables.Node;

/**
 *
 * @author Odd
 */
class Simulation {
    
    /**
     * Can move approx 50 pixels in appsketch.jpg-image.
     */
    private final int walkingSpeedPerSec = 50;
    
    private final int simsPerSec  = 10;
    private final int walkingSpeed = (int) (walkingSpeedPerSec / simsPerSec);
    
    
    
    private DatabaseHandler dbHandler;
    
    private HashMap<String, Object> options;

    public HashMap<String, Object> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, Object> options) {
        this.options = options;
    }
    
    public Simulation(boolean restart, DatabaseHandler dbHandler){
        this.dbHandler = dbHandler;
        if(restart){
            
        }
    }
    
    public Point getNextPosition(){
        Point currentLocation = (Point) options.get("position");
        
        
        
        return currentLocation;
    }
    
    public Point moveTowardsPoint(Node endPoint, Node currentPoint){
        Point nextPoint = currentPoint;
        
        double distance = nextPoint.distance(endPoint);
        int dx = endPoint.x - nextPoint.x;
        int dy = endPoint.y - nextPoint.y;
        if(distance < walkingSpeed) nextPoint.setLocation(endPoint);
        else{
            nextPoint.translate((int) (dx * (walkingSpeed / distance)), 
                    (int) (dy * (walkingSpeed / distance)));
        }
        
        return nextPoint;
    }
}
