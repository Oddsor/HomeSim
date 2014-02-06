
package no.oddsor.simulator3;

import no.oddsor.simulator3.enums.ObjectTypes;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Odd
 */
class Node extends AbstractTable{
    
    private static final String tableName = "node";
    
    private Point location;
    private ArrayList<Node> neighbours;
    public ArrayList<HouseObject> types;

    public Node(SQLiteConnection db, int id, Point location) throws SQLiteException {
        super(db, id, tableName, null);
        this.location = location;
        this.neighbours = new ArrayList<>();
        this.types = new ArrayList<>();
    }
    
    private void addNeighbour(Node node){
        for(Node nod: neighbours){
            if(nod.id == node.id) return;
        }
        neighbours.add(node);
    }

    public Collection<Node> getNeighbours() {
        
        return neighbours;
    }
    
    public double distance(Point p){
        return location.distance(p);
    }
    
    public Point getLocation(){
        return location;
    }
    
    public void setLocation(int x, int y){
        location = new Point(x, y);
    }
    
    public void update(){
        try {
            if(id == -1){
                db.exec("INSERT INTO node VALUES(null, " + location.x + ", " + location.y + ");");
                id = (int)db.getLastInsertId();
                System.out.println("Added new node");
            }
            else{
                db.exec("UPDATE node SET x = " + location.x + ", y=" + location.y + " WHERE id = " + id + ";");
                System.out.println("Updated node's location");
            }
            db.exec("DELETE FROM node_objects WHERE nodeid = " + id);
            for(HouseObject type: types){
                db.exec("INSERT INTO node_objects VALUES(null, " + id + ", '" + type.type + "');");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static ArrayList<Node> getNodes(SQLiteConnection db) {
        ArrayList<Node> nodes = new ArrayList<>();
        try{
            SQLiteStatement st = db.prepare("SELECT * FROM node");
            while (st.step()){
                Node nod = new Node(db, st.columnInt(0), 
                        new Point(st.columnInt(1), st.columnInt(2)));
                ArrayList<HouseObject> types = new ArrayList<>();
                SQLiteStatement st2 = db.prepare("SELECT * FROM node_objects WHERE nodeid = " + st.columnInt(0) + ";");
                while(st2.step()){
                    types.add(new HouseObject(ObjectTypes.valueOf(st2.columnString(2)), nod));
                }
                nod.types = types;
                nodes.add(nod);
            }
            ArrayList<Edge> edges = Edge.getEdges(nodes, db);
            for(Node node: nodes){
                for(Edge edge: edges){
                    if(node.id == edge.a.id) node.addNeighbour(edge.b);
                    else if(node.id == edge.b.id) node.addNeighbour(edge.a);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            return nodes;
        }
    }
    
    public void addObject(ObjectTypes type){
        if(types.add(new HouseObject(type, this))){
            update();
        }
    }
    
    public void removeObject(ObjectTypes type){
        for(HouseObject obj: types){
            if(obj.type == type){
                types.remove(obj);
                update();
            }
        }
    }
}
