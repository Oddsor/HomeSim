
package no.oddsor.simulator3.deprecated;

import com.almworks.sqlite4java.SQLiteConnection;

/**
 *
 * @author Odd
 */
public class Room {
    private int id;
    private String name;
    private int owner;
    private int privateRoom;

    public Room(int id, String name, int owner, int privateRoom) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.privateRoom = privateRoom;
    }
    
    public static Room getRoom(int id, SQLiteConnection db){
        return null;
    }
}
