
package no.oddsor.simulator2.db_tables;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteStatement;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import no.oddsor.simulator3.HouseObjects;

/**
 *
 * @author Odd
 */
public class Node extends Point{

    public static ArrayList<Node> getNodes(SQLiteConnection db) {
        ArrayList<Node> nodes = new ArrayList<>();
        try{
            SQLiteStatement st = db.prepare("SELECT * FROM node");
            while (st.step()){
                Set<HouseObjects> types = new HashSet<>();
                SQLiteStatement st2 = db.prepare("SELECT * FROM node_objects WHERE nodeid = " + st.columnInt(1) + ";");
                while(st2.step()){
                    types.add(HouseObjects.valueOf(st2.columnString(2)));
                }
                nodes.add(new Node(st.columnInt(0), st.columnInt(1), st.columnInt(2)));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            return nodes;
        }
    }
    
    public static void createTable(SQLiteConnection db){
        try {
            db.exec("CREATE TABLE IF NOT EXISTS node (" +
" id INTEGER PRIMARY KEY, x INT, y INT);");
            db.exec("CREATE TABLE IF NOT EXISTS node_objects (id INTEGER PRIMARY KEY, " + 
                    "nodeid INTEGER, type VARCHAR(45));");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public int id;
    public Set<HouseObjects> types;
    
    public Node(int id, int x, int y){
        this.id = id;
        this.x = x;
        this.y = y;
        this.types = new HashSet<>();
    }
    
    public Node(int id, int x, int y, Set<HouseObjects> types){
        this.id = id;
        this.x = x;
        this.y = y;
        this.types = types;
    }
    
    
    public void update(SQLiteConnection db){
        try {
            if(id == -1){
                db.exec("INSERT INTO node VALUES(null, " + x + ", " + y + ");");
                id = (int)db.getLastInsertId();
                System.out.println("Added new node");
            }
            else{
                db.exec("UPDATE node SET x = " + x + ", y=" + y + " WHERE id = " + id + ";");
                
                System.out.println("Updated node's location");
            }
            db.exec("DELETE FROM node_objects WHERE nodeid = " + id);
            for(HouseObjects type: types){
                db.exec("INSERT INTO node_objects VALUES(null, " + id + ", " + type + ");");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void remove(SQLiteConnection db) {
        try{
            db.exec("DELETE FROM node WHERE id=" + id);
            db.exec("DELETE FROM node_objects WHERE nodeid=" + id);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void addObject(HouseObjects type, SQLiteConnection db){
        if(types.add(type)){
            update(db);
        }
    }
    
    public void removeObject(HouseObjects type, SQLiteConnection db){
        if(types.remove(type)){
            update(db);
        }
    }
}