
package no.oddsor.simulator3;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Odd
 */
abstract class AbstractTable{
    
    private static final String INTEGER = "INTEGER";
    private static final String DOUBLE = "DOUBLE";
    private static final String STRING = "STRING";
    
    SQLiteConnection db;
    int id;
    
    private String tableName;
    private Map<String, String> columns;
    
    public AbstractTable(SQLiteConnection db, int id, String tableName, 
            Map<String, String> columns) throws SQLiteException{
        if(!db.isOpen()) db.open();
        this.db = db;
        this.id = id;
        this.tableName = tableName;
        this.columns = columns;
    }
    
    private static void createSimpleTable(String tableName, Map<String, String> columns, 
            boolean ifNotExists, SQLiteConnection dbConn) throws SQLiteException {
        String sql = "CREATE TABLE " + (ifNotExists ? "IF NOT EXISTS ": "") + tableName + "(";
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