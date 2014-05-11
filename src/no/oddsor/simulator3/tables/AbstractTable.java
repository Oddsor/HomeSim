
package no.oddsor.simulator3.tables;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;

/**
 *
 * @author Odd
 */
abstract class AbstractTable{
    
    
    SQLiteConnection db;
    public int id;
    
    private String tableName;
    
    public AbstractTable(SQLiteConnection db, int id, String tableName) throws SQLiteException{
        if(!db.isOpen()) db.open();
        this.db = db;
        this.id = id;
        this.tableName = tableName;
    }
    
    public boolean remove(){
        if(id == -1) return false;
        try {
            db.exec("DELETE FROM " + tableName + " WHERE id=" + id + ";");
            if(db.getChanges() != 1) throw new Exception("Possible error with deletion?");
            else return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}