
package no.oddsor.simulator2.db_tables;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Odd
 */
abstract class AbstractTable implements DatabaseTable{
    
    private static final String INTEGER = "INTEGER";
    private static final String DOUBLE = "DOUBLE";
    private static final String STRING = "STRING";
    
    private SQLiteConnection db;
    private int id;
    
    public AbstractTable(SQLiteConnection db, int id) throws SQLiteException{
        if(!db.isOpen()) db.open();
        this.db = db;
        this.id = id;
    }
    
    public static void createSimpleTable(String tableName, Map<String, String> columns, 
            boolean ifNotExists, SQLiteConnection dbConn) throws SQLiteException {
        String sql = "CREATE TABLE " + (ifNotExists ? "IF NOT EXISTS ": "") + tableName + " (";
        sql += ("id INTEGER PRIMARY KEY, ");
        Iterator it = columns.keySet().iterator();
        while(it.hasNext()){
            String colName = (String) it.next();
            if(columns.get(colName).equals(STRING)) sql += (colName + " '" + columns.get(colName) + "'");
            else sql += (colName + " " + columns.get(colName));
            if(it.hasNext()) sql += ", ";
            else sql += ");";
        }
        dbConn.open();
        dbConn.exec(sql);
    }
    
    public void remove(int id, String tableName){
        if(this.id == -1) return;
        try {
            db.exec("DELETE FROM " + tableName + " WHERE id=" + id + ";");
            if(db.getChanges() != 1) throw new Exception("Possible error with deletion?");
            else this.id = -1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
