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
public class Task {

    public static ArrayList<Task> getTasks(DatabaseHandler dbHandler) {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteConnection db = dbHandler.getDb();
        try{
            db.open();
            SQLiteStatement st = db.prepare("SELECT * FROM task");
            while(st.step()){
                Task task = new Task(st.columnInt(0), st.columnString(1), 
                        st.columnInt(2), st.columnInt(3));
                task.addGivenStatuses(StatusModification.getModsForTask(task, StatusModification.GIVEN, dbHandler));
                task.addNeededStatuses(StatusModification.getModsForTask(task, StatusModification.NEEDED, dbHandler));
                tasks.add(task);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
            return tasks;
        }
    }

    public int id;
    public String taskName;
    public int pausable;
    public int duration;
    public ArrayList<StatusModification> givenStatus;
    public ArrayList<StatusModification> neededStatus;
    public ArrayList<VariableModification> givenVariables;
    public ArrayList<VariableModification> neededVariables;

    public Task(int id, String taskName, int pausable, int duration) {
        this.id = id;
        this.taskName = taskName;
        this.pausable = pausable;
        this.duration = duration;
    }
    
    public void addGivenStatuses(ArrayList<StatusModification> statuses){
        givenStatus = statuses;
    }
    public void addNeededStatuses(ArrayList<StatusModification> statuses){
        neededStatus = statuses;
    }
    
    public void addGivenVariables(ArrayList<VariableModification> variables){
        this.givenVariables = variables;
    }
    
    public void addNeededVariables(ArrayList<VariableModification> variables){
        this.neededVariables = variables;
    }
    
    public static void createTable(DatabaseHandler dbHandler){
        SQLiteConnection db = dbHandler.getDb();
        try {
            db.open(true);
            db.exec("CREATE TABLE IF NOT EXISTS task (" +
" id INTEGER PRIMARY KEY, taskName VARCHAR(45) UNIQUE, pausable INT, duration INT);");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
        }
    }
    
    public static Task getTaskById(int id, DatabaseHandler dbHandler) {
        SQLiteConnection db = dbHandler.getDb();
        Task task = null;
        try{
            db.open();
            SQLiteStatement st = db.prepare("SELECT * FROM task WHERE id = " + id);
            if(st.step()){
                task = new Task(st.columnInt(0), st.columnString(1), st.columnInt(2), st.columnInt(3));
                task.addGivenStatuses(StatusModification.getModsForTask(task, StatusModification.GIVEN, dbHandler));
                task.addNeededStatuses(StatusModification.getModsForTask(task, StatusModification.NEEDED, dbHandler));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
            return task;
        }
    }
    
    public static void insertTask(Task task, DatabaseHandler dbHandler){
        SQLiteConnection db = dbHandler.getDb();
        try{
            db.open();
            db.exec("INSERT INTO task VALUES(null, '" + task.taskName + "', " + task.pausable + ", " + task.duration +")");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
        }
    }
}
