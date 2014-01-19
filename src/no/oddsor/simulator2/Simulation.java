/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator2;

import java.util.ArrayList;

/**
 *
 * @author Odd
 */
class Simulation {
    
    private DatabaseHandler dbHandler;
    private ArrayList<Object> gameVariables;

    //TODO add all variables
    public ArrayList<Object> getGameVariables() {
        return gameVariables;
    }

    //TODO save all variables
    public void setGameVariables(ArrayList<Object> gameVariables) {
        this.gameVariables = gameVariables;
    }
    
    public Simulation(boolean restart, DatabaseHandler dbHandler){
        this.dbHandler = dbHandler;
        if(restart){
            
        }
    }
    

}
