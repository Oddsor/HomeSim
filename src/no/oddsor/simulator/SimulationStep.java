/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import no.oddsor.simulator.db_tables.Path;
import no.oddsor.simulator.db_tables.Status;
import no.oddsor.simulator.db_tables.Task;
import no.oddsor.simulator.db_tables.helpers.PathPoint;

/**
 *
 * @author Odd
 */
public class SimulationStep {
    
    ArrayList<Status> status;
    ArrayList<Task> tasks;
    IPerson p;
    DatabaseHandler dbHandler;
    double energy = 100.0;
    double hunger = 100.0;
    
    int food = 0;
    
    Point startposition;
    Point position;
    Path currentPath;
    
    ArrayList<Point> route;
    
    Task endGoal;
    Task currentGoal;
    
    
    public SimulationStep(Point startpos) throws IOException{
        dbHandler = new DatabaseHandler();
        status = Status.getStatus(dbHandler);
        tasks = Task.getTasks(dbHandler);
        startposition = startpos;
        position = new Point(startpos);
        System.out.println("HI");
    }
    
    public Point nextStep(){
        if(endGoal == null){
            endGoal = findEndGoal();
            if(endGoal != null) System.out.println("Endtask: " + endGoal.taskName);
        }
        if(currentGoal == null){
            currentGoal = findRequiredTasks(endGoal);
            if(currentGoal != null) System.out.println("Task: " + currentGoal.taskName);
        }
        
        if(currentGoal != null){
            if(route == null){
                route = findRouteToTask(currentGoal, null, null, position);
                Collections.reverse(route);
            }
            else{
                if(!route.isEmpty()){
                    position = route.get(0);
                    route.remove(0);
                }else{
                    route = null;
                    if(currentGoal.id == 2){
                        food = 1;
                    }else if(currentGoal.id == 1){
                        energy = 100.0;
                    }else if(currentGoal.id == 3){
                        food = 0;
                        hunger = 100.0;
                    }
                    System.out.println("Hit end of task " + currentGoal.taskName);
                    if(currentGoal == endGoal) endGoal = null;
                    currentGoal = null;
                }
            }
        }
        hunger -= 1.0;
        energy -= 0.5;
        return position;
    }
    
    private Task findRequiredTasks(Task task){
        if(task == null) return null;
        if(task.id == 3 && food == 0) return tasks.get(1);
        else return task;
    }
    
    private Task findEndGoal(){
        if(energy < 10.0){
            System.out.println("returning: " + tasks.get(0).taskName);
            return tasks.get(0);
        }
        if(hunger < 30.0){
            System.out.println("returning: " + tasks.get(0).taskName);
            return tasks.get(2);
        }
        else return null;
    }

    private ArrayList<Point> findRouteToTask(Task currentGoal, Path startingPath, Point connecting, Point avatarPos) {
        if(startingPath == null){ 
            ArrayList<Path> paths = Path.getPaths(dbHandler);
            ArrayList<Point> points = new ArrayList<>();
            for(int i = 0; i < paths.size(); i++){
                if(paths.get(i).task != null && paths.get(i).task.id == currentGoal.id){ 
                    startingPath = paths.get(i);
                    break;
                }
            }
        }
        ArrayList<Point> points = new ArrayList<>();
        if(connecting == null) points = pathToPoints(startingPath, false);
        else{
            if((connecting.x < startingPath.endX + 10 && connecting.x > startingPath.endX - 10) &&
                    (connecting.y < startingPath.endY + 10 && connecting.y > startingPath.endY - 10)){
                points = pathToPoints(startingPath, false);
            }else points = pathToPoints(startingPath, true);
        }
        ArrayList<Path> nextPaths = startingPath.getPathsFromCoord(
                points.get(points.size() - 1).x, 
                points.get(points.size() - 1).y, 
                dbHandler, 10);
        if(nextPaths.size() == 0) return points;
        else{
            for(Path nextpath: nextPaths){
                ArrayList<Point> newpoints = findRouteToTask(currentGoal, nextpath, 
                        points.get(points.size() - 1), avatarPos);
                Point endPoint = newpoints.get(newpoints.size() - 1);
                if((endPoint.x < avatarPos.x + 10 && endPoint.x > avatarPos.x - 10) &&
                        (endPoint.y < avatarPos.y + 10 && endPoint.y > avatarPos.y - 10)){
                    points.addAll(newpoints);
                }
            }
        }
        return points;
    }
    
    private ArrayList<Point> pathToPoints(Path path, boolean reverse){
        ArrayList<Point> points = new ArrayList<Point>();
        ArrayList<PathPoint> pathpoints = path.points;
        if(!reverse) Collections.reverse(pathpoints);
        for(int i = 0; i < pathpoints.size(); i++){
            points.add(new Point(pathpoints.get(i).x, pathpoints.get(i).y));
        }
        return points;
    }
}
