
package no.oddsor.simulator2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import no.oddsor.simulator2.db_tables.Node;
import no.oddsor.simulator2.db_tables.Options;

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
    
    private HashMap<Integer, Object> options;
    private ArrayList<Person> people;

    public HashMap<Integer, Object> getOptions() {
        return options;
    }

    public void setOptions(HashMap<Integer, Object> options) {
        this.options = options;
    }
    
    //TODO implement some sort of resume-feature (if restart false)
    public Simulation(boolean restart, DatabaseHandler dbHandler){
        this.dbHandler = dbHandler;
        this.people = Person.getPeople((Point) options.get(Options.START_LOCATION));
        
        //TODO what do we need to start a sim?! The map, the points belonging to map
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
