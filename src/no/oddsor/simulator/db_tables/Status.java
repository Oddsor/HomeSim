
package no.oddsor.simulator.db_tables;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteStatement;
import java.util.ArrayList;
import no.oddsor.simulator.DatabaseHandler;

/**
 *
 * @author Odd
 */
public class Status {

    public static Status getStatusById(int columnInt, DatabaseHandler dbHandler) {
        SQLiteConnection db = dbHandler.getDb();
        Status status = null;
        try{
            db.open();
            SQLiteStatement st = db.prepare("SELECT * FROM status WHERE id = " + columnInt);
            if(st.step())status = new Status(st.columnInt(0), st.columnString(1), st.columnDouble(2));
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
            return status;
        }
    }

    public static ArrayList<Status> getStatus(DatabaseHandler dbHandler) {
        SQLiteConnection db = dbHandler.getDb();
        ArrayList<Status> statuses = new ArrayList<>();
        try{
            db.open();
            SQLiteStatement st = db.prepare("SELECT * FROM status");
            while(st.step()){
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
            return statuses;
        }
    }
    public int id;
    public String statusName;
    public double decreaseRate;

    public Status(int id, String statusName, double decreaseRate) {
        this.id = id;
        this.statusName = statusName;
        this.decreaseRate = decreaseRate;
    }
    
    public static void createTable(DatabaseHandler dbHandler){
        SQLiteConnection db = dbHandler.getDb();
        try {
            db.open(true);
            db.exec("CREATE TABLE IF NOT EXISTS status (" +
" id INTEGER PRIMARY KEY, name VARCHAR(45), decreaseRate DOUBLE);");
            db.dispose();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
        }
    }
}
