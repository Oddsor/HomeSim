/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator2.db_tables;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteStatement;
import java.awt.Point;
import java.util.ArrayList;

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
                nodes.add(new Node(st.columnInt(0), st.columnInt(1), st.columnInt(2)));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            return nodes;
        }
    }
    
    public static Node getNodeById(int id, SQLiteConnection db){
        try{
            SQLiteStatement st = db.prepare("SELECT * FROM node WHERE id = " + id);
            if(st.step()){
                Node node = new Node(st.columnInt(0), st.columnInt(1), st.columnInt(2));
                return node;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    
    public static void createTable(SQLiteConnection db){
        try {
            db.exec("CREATE TABLE IF NOT EXISTS node (" +
" id INTEGER PRIMARY KEY, x INT, y INT);");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public int id;
    
    public Node(int id, int x, int y){
        this.id = id;
        this.x = x;
        this.y = y;
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
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void remove(SQLiteConnection db) {
        try{
            db.exec("DELETE FROM node WHERE id=" + id);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
