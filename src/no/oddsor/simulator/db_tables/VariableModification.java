
package no.oddsor.simulator.db_tables;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteStatement;
import java.util.ArrayList;
import no.oddsor.simulator.DatabaseHandler;

/**
 *
 * @author Odd
 */
public class VariableModification {
    
    

    public static ArrayList<VariableModification> getVarsForTask(Task task, boolean table, DatabaseHandler dbHandler) {
        SQLiteConnection db = dbHandler.getDb();
        ArrayList<VariableModification> mods = new ArrayList<>();
        try{
            db.open();
            SQLiteStatement st = db.prepare("SELECT * FROM " + 
                    (table == StatusModification.NEEDED ? "task_needs_var" : "task_gives_var") + 
                    " WHERE idtask = " + task.id);
            while(st.step()){
                mods.add(new VariableModification(task, WorldVariables.getVarById(st.columnInt(2), dbHandler), st.columnDouble(3)));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
            return mods;
        }
    }

    public Task task;
    public WorldVariables var;
    public double modification;
    
    public static final boolean GIVEN = false;
    public static final boolean NEEDED = true;

    public VariableModification(Task task, WorldVariables var, double modification) {
        this.task = task;
        this.var = var;
        this.modification = modification;
    }
    
    public static void createTable(DatabaseHandler dbHandler){
        SQLiteConnection db = dbHandler.getDb();
        try {
            db.open(true);
            db.exec("CREATE TABLE IF NOT EXISTS task_needs_var (" +
"id INTEGER PRIMARY KEY, idtask INTEGER, idvar INTEGER, value_needed INT);");
            db.exec("CREATE TABLE IF NOT EXISTS task_gives_var (" +
"id INTEGER PRIMARY KEY, idtask INTEGER, idvar INTEGER, value_given INT);");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
        }
    }
}