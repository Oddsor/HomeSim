
package no.oddsor.simulator2.db_tables;

import java.util.ArrayList;
import no.oddsor.simulator3.enums.ObjectTypes;

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
    ArrayList<ObjectTypes> types;
    
    public ObjectLocation(int id, Node node, ArrayList<ObjectTypes> types){
        this.id = id;
        this.node = node;
        this.types = types;
    }
    
    public void addType(ObjectTypes type){
        types.add(type);
    }
    
    public ArrayList<ObjectTypes> getTypes(){
        return types;
    }
    
    public void update(){
        
    }
}