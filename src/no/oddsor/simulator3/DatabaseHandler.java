
package no.oddsor.simulator3;

import no.oddsor.simulator3.tables.Node;
import no.oddsor.simulator3.tables.Edge;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Odd
 */
public class DatabaseHandler {
    
    String folder;
    public DatabaseHandler(String folder) throws IOException{
        this.folder = folder;
        //new File(folder + "/database.sqlite").mkdirs();
        new File(folder + "/database.sqlite").createNewFile();
        createTables();
    }
    
    public SQLiteConnection getDb(){
        return new SQLiteConnection(new File(folder + "/database.sqlite"));
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
