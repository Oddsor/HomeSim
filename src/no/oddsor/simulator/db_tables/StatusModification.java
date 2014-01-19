/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator.db_tables;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteStatement;
import java.util.ArrayList;
import no.oddsor.simulator.DatabaseHandler;

/**
 *
 * @author Odd
 */
public class StatusModification {
    
    

    public static ArrayList<StatusModification> getModsForTask(Task task, boolean table, DatabaseHandler dbHandler) {
        SQLiteConnection db = dbHandler.getDb();
        ArrayList<StatusModification> mods = new ArrayList<>();
        try{
            db.open();
            SQLiteStatement st = db.prepare("SELECT * FROM " + 
                    (table == StatusModification.NEEDED ? "task_needs_status" : "task_gives_status") + 
                    " WHERE idtask = " + task.id);
            while(st.step()){
                mods.add(new StatusModification(task, Status.getStatusById(st.columnInt(2), dbHandler), st.columnDouble(3)));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
            return mods;
        }
    }

    public Task task;
    public Status status;
    public double modification;
    
    public static final boolean GIVEN = false;
    public static final boolean NEEDED = true;

    public StatusModification(Task task, Status status, double modification) {
        this.task = task;
        this.status = status;
        this.modification = modification;
    }
    
    public static void createTable(DatabaseHandler dbHandler){
        SQLiteConnection db = dbHandler.getDb();
        try {
            db.open(true);
            db.exec("CREATE TABLE IF NOT EXISTS task_needs_status (" +
"id INTEGER PRIMARY KEY, idtask INTEGER, idstatus INTEGER, value_needed DOUBLE);");
            db.exec("CREATE TABLE IF NOT EXISTS task_gives_status (" +
"id INTEGER PRIMARY KEY, idtask INTEGER, idstatus INTEGER, value_needed DOUBLE);");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
        }
    }
}
