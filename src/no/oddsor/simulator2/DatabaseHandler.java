/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator2;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.oddsor.simulator2.db_tables.Edge;
import no.oddsor.simulator2.db_tables.Node;

/**
 *
 * @author Odd
 */
public class DatabaseHandler {
    
    private static final String standardName = "database.sqlite";
    private String filename;
    
    public DatabaseHandler(String filename) throws IOException{
        new File(filename).mkdirs();
        new File(filename).createNewFile();
        this.filename = filename;
        createTables();
    }
    
    public SQLiteConnection getDb(){
        return new SQLiteConnection(new File(filename));
    }
    
    public DatabaseHandler() throws IOException{
        new File(standardName).createNewFile();
        this.filename = standardName;
        createTables();
    }
    
    private void createTables(){
        SQLiteConnection db = this.getDb();
        try {
            db.open();
            Node.createTable(db);
            Edge.createTable(db);
        } catch (SQLiteException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            db.dispose();
        }
    }
}
