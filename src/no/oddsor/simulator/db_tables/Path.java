/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator.db_tables;

import no.oddsor.simulator.db_tables.helpers.PathPoint;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import java.util.ArrayList;
import no.oddsor.simulator.DatabaseHandler;

/**
 *
 * @author Odd
 */
public class Path {
    
    final int standardThreshold = 5;
    
    public int pathId, startX, startY, endX, endY;
    public Task task;
    public ArrayList<PathPoint> points;
    
    public Path(int pathId, int startX, int startY, int endX, int endY, 
            Task task, ArrayList<PathPoint> points){
        this.pathId = pathId;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.points = points;
        this.task = task;
    }
    
    public static void createTable(DatabaseHandler dbHandler){
        SQLiteConnection db = dbHandler.getDb();
        try {
            db.open(true);
            db.exec("CREATE TABLE IF NOT EXISTS path (" +
" id INTEGER PRIMARY KEY, startX INT, startY INT, endX INT, endY INT," +
"startedTask INT, points LONGTEXT);");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
        }
    }
    
    public static Path getPathById(int id, DatabaseHandler dbHandler){
        SQLiteConnection db = dbHandler.getDb();
        Path path = null;
        try{
            db.open();
            SQLiteStatement st = db.prepare("SELECT * FROM path WHERE id = " + id);
            if (st.step()){ path = new Path(st.columnInt(0), st.columnInt(1), 
                    st.columnInt(2), st.columnInt(3), st.columnInt(4), 
                    Task.getTaskById(st.columnInt(5), dbHandler), PathPoint.parsePoints(st.columnString(6)));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
            return path;
        }
    }
    
    public static ArrayList<Path> getPaths(DatabaseHandler dbHandler){
        SQLiteConnection db = dbHandler.getDb();
        ArrayList<Path> paths = new ArrayList<>();
        try{
            db.open();
            SQLiteStatement st = db.prepare("SELECT * FROM path");
            while(st.step()){ 
                paths.add(new Path(st.columnInt(0), st.columnInt(1), 
                    st.columnInt(2), st.columnInt(3), st.columnInt(4), 
                    Task.getTaskById(st.columnInt(5), dbHandler), PathPoint.parsePoints(st.columnString(6))));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
            return paths;
        }
    }
    
    public static void insertPath(int startX, int startY, int endX, int endY, Task task,
            String points, DatabaseHandler dbHandler){
        SQLiteConnection db = dbHandler.getDb();
        try{
            db.open(true);
            db.exec("INSERT INTO path VALUES(null, " + startX + ", " + startY + 
                    ", " + endX + ", " + endY + ", " + 
                    (task == null? null : task.id) + ", '" + points + "');");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
        }
    }
    
    public ArrayList<Path> getPathsFromCoord(int x, int y, DatabaseHandler dbHandler, int... threshold){
        int newThreshold = standardThreshold;
        if(threshold != null) newThreshold = threshold[0];
        SQLiteConnection db = dbHandler.getDb();
        ArrayList<Path> paths = null;
        try {
            db.open(true);
            SQLiteStatement st = db.prepare("SELECT * FROM paths WHERE (startX <= " + (x + newThreshold) + 
                    " AND startX >= " + (x - newThreshold) + 
                    " AND startY <= " + (y + newThreshold) +
                    " AND startY >= " + (y - newThreshold) + ") OR "
                    + "(endX <=  " + (x + newThreshold) + 
                    " AND endX >= " + (x - newThreshold) + 
                    " AND endY <= " + (y + newThreshold) + 
                    " AND endY >= " + (y - newThreshold) + ");");
            paths = new ArrayList<>();
            
            while(st.step()){
                paths.add(new Path(st.columnInt(0), st.columnInt(1), st.columnInt(2),
                        st.columnInt(3), st.columnInt(4), Task.getTaskById(st.columnInt(5), dbHandler), 
                        PathPoint.parsePoints(st.columnString(6))));
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }finally{
            db.dispose();
            return paths;
        }
    }
}
