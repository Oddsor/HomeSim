
package no.oddsor.simulator2.db_tables;

import java.util.ArrayList;
import no.oddsor.simulator3.HouseObjects;

/**
 *
 * @author Odd
 */
public class ObjectLocation{
    
    public final String tableName = "node_has_object";
    public final String[][] columns = {
        {"nodeid", "VARCHAR(45)"},
        {"type", "VARCHAR(45)"}
    };
    
    int id;
    Node node;
    ArrayList<HouseObjects> types;
    
    public ObjectLocation(int id, Node node, ArrayList<HouseObjects> types){
        this.id = id;
        this.node = node;
        this.types = types;
    }
    
    public void addType(HouseObjects type){
        types.add(type);
    }
    
    public ArrayList<HouseObjects> getTypes(){
        return types;
    }
    
    public void update(){
        
    }
}