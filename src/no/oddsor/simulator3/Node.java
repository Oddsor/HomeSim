
package no.oddsor.simulator3;

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

    public Node(SQLiteConnection db, int id, Point location) throws SQLiteException {
        super(db, id, tableName, null);
        this.location = location;
        ArrayList<Node> neighbours = new ArrayList<>();
    }
    
    private void addNeighbour(Node node){
        for(Node nod: neighbours){
            if(nod.id == node.id) return;
        }
        neighbours.add(node);
    }

    Collection<Node> getNeighbours() {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public double distance(Point p){
        return location.distance(p);
    }
    
    public Point getLocation(){
        return location;
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
}
