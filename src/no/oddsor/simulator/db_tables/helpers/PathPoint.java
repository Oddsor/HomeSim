
package no.oddsor.simulator.db_tables.helpers;

import java.util.ArrayList;

/**
 *
 * @author Odd
 */
public class PathPoint {
    
    public int timeOffset, x, y;
    
    public PathPoint(int timeOffset, int x, int y){
        this.timeOffset = timeOffset;
        this.x = x;
        this.y = y;
    }
    
    public static ArrayList<PathPoint> parsePoints(String points){
        ArrayList<PathPoint> pointList = new ArrayList<>();
        String[] split = points.split("\n");
        for(int i = 0; i < split.length; i++){
            String[] newSplit = split[i].split(";");
            pointList.add(new PathPoint(Integer.parseInt(newSplit[0]), 
                    Integer.parseInt(newSplit[1]), Integer.parseInt(newSplit[2])));
        }
        return pointList;
    }
}
