/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.oddsor.simulator;

import java.util.ArrayList;
import no.oddsor.simulator.db_tables.Status;
import no.oddsor.simulator.db_tables.StatusModification;
import no.oddsor.simulator.db_tables.Task;
import no.oddsor.simulator.db_tables.VariableModification;
import no.oddsor.simulator.db_tables.WorldVariables;

/**
 *
 * @author Odd
 */
public class FastSetup {
    public static void main(String[] args){
        try{
            DatabaseHandler dbHandler = new DatabaseHandler();
            
            Status energy = new Status(0, "Energy", 1.0);
            Status hungry = new Status(1, "Hungry", 10.0);
            WorldVariables food =  new WorldVariables(0, "Food made", 1);
            //TODO separate variables and desires
            
            Task sleepTask = new Task(0, "Go to bed", 0, 100);
            Task cookTask = new Task(1, "Make food", 0, 5);
            Task eatTask = new Task(2, "Eat food", 0, 5);
            
            StatusModification sleepMod = new StatusModification(sleepTask, energy, 100.0);
            
            ArrayList<StatusModification> modsForSleeping = new ArrayList<>();
            modsForSleeping.add(sleepMod);
            sleepTask.addGivenStatuses(modsForSleeping);
            
            VariableModification foodMod = new VariableModification(cookTask, food, 1);
            ArrayList<VariableModification> varsFromCooking = new ArrayList<>();
            varsFromCooking.add(foodMod);
            cookTask.addGivenVariables(varsFromCooking);
            
            
            StatusModification eatMod = new StatusModification(eatTask, hungry, 70.0);
            VariableModification consumeFood = new VariableModification(eatTask, food, 0);
            
            /*ArrayList<StatusModification> modsForEating = new ArrayList<>();
            ArrayList<StatusModification> neededForEating = new ArrayList<>();
            modsForEating.add(allMods.get(allMods.size() - 2));
            neededForEating.add(allMods.get(allMods.size() - 1));
            tasks.get(2).addGivenStatuses(modsForEating);
            tasks.get(2).addNeededStatuses(allMods);*/
            
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
