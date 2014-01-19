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
public class WorldVariables {
    
    public static WorldVariables getVarById(int columnInt, DatabaseHandler dbHandler) {
        SQLiteConnection db = dbHandler.getDb();
        WorldVariables variable = null;
        try{
            db.open();
            SQLiteStatement st = db.prepare("SELECT * FROM status WHERE id = " + columnInt);
            if(st.step())variable = new WorldVariables(st.columnInt(0), st.columnString(1), st.columnInt(2));
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
            return variable;
        }
    }

    public static ArrayList<WorldVariables> getVariables(DatabaseHandler dbHandler) {
        SQLiteConnection db = dbHandler.getDb();
        ArrayList<WorldVariables> variables = new ArrayList<>();
        try{
            db.open();
            SQLiteStatement st = db.prepare("SELECT * FROM variable");
            while(st.step()){
                variables.add(new WorldVariables(st.columnInt(0), st.columnString(1), st.columnInt(2)));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
            return variables;
        }
    }
    public int id;
    public String variableName;
    public int maxVal;

    public WorldVariables(int id, String variableName, int maxVal) {
        this.id = id;
        this.variableName = variableName;
        this.maxVal = maxVal;
    }
    
    public static void createTable(DatabaseHandler dbHandler){
        SQLiteConnection db = dbHandler.getDb();
        try {
            db.open(true);
            db.exec("CREATE TABLE IF NOT EXISTS variable (" +
" id INTEGER PRIMARY KEY, name VARCHAR(45), maxVal INT);");
            db.dispose();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.dispose();
        }
    }
}
