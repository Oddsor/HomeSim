
package no.oddsor.simulator2;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;
import no.oddsor.simulator2.db_tables.Needs;

/**
 *
 * @author Odd
 */
public class Person {

    static ArrayList<Person> getPeople(Point startPosition) {
        ArrayList<Person> people = new ArrayList<>();
        
        people.add(new Person(startPosition, "Odd", null));
        
        return people;
    }
    
    private Point position;
    private String name;
    private Image avatar;
    private Map<Integer, Needs> needs;
    
    public Person(Point currentPosition, String name, Image avatar){
        this.position = currentPosition;
        this.needs = Needs.getNeeds();
        this.avatar = avatar;
    }
    
    /**
     * 
     * @return 
     */
    //public Task getTargetTask();
    
}