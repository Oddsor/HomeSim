
package no.oddsor.simulator3;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteStatement;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Odd
 */
public class Edge {

    public static ArrayList<Edge> getEdges(ArrayList<Node> nodes, SQLiteConnection db) {
        ArrayList<Edge> edges = new ArrayList<>();
        try{
            SQLiteStatement st = db.prepare("SELECT * FROM edge");
            while(st.step()){
                Node a = null;
                Node b = null;
                for(Node current: nodes){
                    if(current.id == st.columnInt(1)) a = current;
                    else if(current.id == st.columnInt(2)) b = current;
                }
                edges.add(new Edge(st.columnInt(0), a, b));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            return edges;
        }
    }

    public static void createTable(SQLiteConnection db){
        try {
            db.exec("CREATE TABLE IF NOT EXISTS edge (" +
" id INTEGER PRIMARY KEY, idnodea INT, idnodeb INT);");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public int id;
    public Node a, b;
    
    public double cachedLength = -1.0;
    
    public Edge(int id, Node a, Node b){
        this.id = id;
        this.a = a;
        this.b = b;
    }

    public double getLength(){
        if(cachedLength == -1.0){
            cachedLength = Math.sqrt(Math.exp(a.getLocation().x - b.getLocation().x) + 
                    Math.exp(a.getLocation().y - b.getLocation().y));
        }
        return cachedLength;
    }

    public void insertEdge(SQLiteConnection db){
        try{
            if(id == -1) db.exec("INSERT INTO edge VALUES(null, " + a.id + ", " + b.id + ");");
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void removeEdge(SQLiteConnection db){
        try{
            db.exec("DELETE FROM edge WHERE id=" + id + ";");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public boolean exists(ArrayList<Edge> edges, Point p1, Point p2){
        boolean result = false;
        if(edges.isEmpty()) return false;
        for(Edge edge: edges){
            if((edge.a.equals(p1) && edge.b.equals(p2)) || (edge.b.equals(p1) && edge.a.equals(p2))){
                result = true;
                break;
            }
        }
        return result;
    }
    
    public void update(SQLiteConnection db){
        try {
            if(id == -1){
                db.exec("INSERT INTO edge VALUES(null, " + a.id + ", " + b.id + ");");
                id = (int)db.getLastInsertId();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
        }
    }
}
