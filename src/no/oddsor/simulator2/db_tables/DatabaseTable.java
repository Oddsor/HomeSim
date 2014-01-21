
package no.oddsor.simulator2.db_tables;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 *
 * @author Odd
 */
public interface DatabaseTable {
    public ArrayList<Object> getRows();
    
    public ArrayList<Object> getRows(AbstractMap.SimpleImmutableEntry<String, Object> whereXisY);
}
