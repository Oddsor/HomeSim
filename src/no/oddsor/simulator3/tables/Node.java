
package no.oddsor.simulator3.tables;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.oddsor.AStarMulti.AStarNode;
import no.oddsor.simulator3.Appliance;

/**
 *
 * @author Odd
 */
public class Node extends AbstractTable implements AStarNode{
    
    private static final String tableName = "node";
    
    private Point location;
    private final Collection<Node> neighbours;
    public ArrayList<Appliance> types;

    public Node(SQLiteConnection db, int id, Point location) throws SQLiteException {
        super(db, id, tableName);
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
                db.exec("INSERT INTO node VALUES(1, null, " + location.x + ", " + location.y + ");");
                id = (int)db.getLastInsertId();
                System.out.println("Added new node");
            }
            else{
                db.exec("UPDATE node SET x = " + location.x + ", y=" + location.y + " WHERE id = " + id + ";");
                System.out.println("Updated node's location");
            }
            db.exec("DELETE FROM node_objects WHERE nodeid = " + id);
            for(Appliance type: types){
                db.exec("INSERT INTO node_objects VALUES(null, " + id + ", '" + type.type() + "', '');");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void updatePoses(Appliance app, String poseString){
        try{
            db.exec("UPDATE node_objects SET poses='" + poseString + "' WHERE nodeid=" + id + " AND type ='" + app.type() + "'");
            app.setPoses(poseString);
        }catch(Exception e){
            System.out.println("Failed to update poses?");
            e.printStackTrace();
            
        }
    }
    
    public static ArrayList<Node> getNodes(SQLiteConnection db) {
        ArrayList<Node> nodes = new ArrayList<>();
        try{
            SQLiteStatement st = db.prepare("SELECT * FROM node");
            while (st.step()){
                Node nod = new Node(db, st.columnInt(1), 
                        new Point(st.columnInt(2), st.columnInt(3)));
                ArrayList<Appliance> types = new ArrayList<>();
                SQLiteStatement st2 = db.prepare("SELECT * FROM node_objects WHERE nodeid = " + st.columnInt(1) + ";");
                while(st2.step()){
                    String poses = st2.columnString(3);
                    if(poses != null){
                        String[] poseVals = poses.split(" ");
                        Set<String> poseSet = new HashSet<>(Arrays.asList(poseVals));
                        types.add(new Appliance(st2.columnString(2), nod, poseSet));
                    }else types.add(new Appliance(st2.columnString(2), nod));
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
        }return nodes;
    }
    
    public static void createTable(SQLiteConnection db){
        try {
            db.exec("CREATE TABLE IF NOT EXISTS node (" +
"room INT, id INTEGER PRIMARY KEY, x INT, y INT);");
            db.exec("CREATE TABLE IF NOT EXISTS node_objects (id INTEGER PRIMARY KEY, " + 
                    "nodeid INTEGER, type VARCHAR(45), poses VARCHAR(1000));");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void addObject(String type){
        if(types.add(new Appliance(type, this))){
            update();
        }
    }
    
    public void removeObject(String type){
        Iterator<Appliance> it = types.iterator();
        while(it.hasNext()){
            Appliance obj = it.next();
            if(obj.type.equals(type)){
                it.remove();
                update();
            }
        }
    }

    @Override
    public Collection<Node> getNeighbours() {
        return neighbours;
    }

    @Override
    public double getDistance(AStarNode node) throws Exception{
        if(node instanceof Node){
            Node nodeTemp = (Node) node;
            double distance = nodeTemp.distance(this.location);
            return distance;
        }else throw new Exception("Incompatible classtype " + node.getClass());
    }
}