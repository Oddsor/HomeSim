/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator;

import no.oddsor.simulator.db_tables.Task;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import no.oddsor.simulator.db_tables.Path;
import no.oddsor.simulator.db_tables.Status;
import no.oddsor.simulator.db_tables.StatusModification;

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
        Task.createTable(this);
        Path.createTable(this);
        Status.createTable(this);
        StatusModification.createTable(this);
    }
}
