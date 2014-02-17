
package no.oddsor.simulator3;

import no.oddsor.simulator3.enums.NeedType;
import no.oddsor.simulator3.enums.ObjectTypes;
import java.util.ArrayList;
import no.oddsor.simulator3.enums.Item;


/**
 *
 * @author Odd
 */
public class TaskSingleton {
    
    private static TaskSingleton obj;
    
    ArrayList<Task> tasks;
    
    private TaskSingleton(){
        
        tasks = new ArrayList<>();
        Task getIngredients = new Task("Get ingredients from fridge", 10, null, 0, 0, ObjectTypes.REFRIDGERATOR);
        getIngredients.addResultingItem(true, Item.RAW_FOOD, 1);
        getIngredients.addRequiredItem(false, Item.RAW_FOOD, 1);
        tasks.add(getIngredients);
        
        Task cookFood = new Task("Cook food on oven", 60*5, null, 15, 19, ObjectTypes.OVEN);
        cookFood.addRequiredItem(true, Item.RAW_FOOD, 1);
        cookFood.addResultingItem(false, Item.COOKED_FOOD, 1);
        tasks.add(cookFood);
        
        Task eatDinner = new Task("Eat cooked food", 60*5, null, 15, 19, ObjectTypes.CHAIR);
        eatDinner.addRequiredItem(true, Item.COOKED_FOOD, 1);
        eatDinner.addResult(NeedType.HUNGER, 100);
        tasks.add(eatDinner);
        
        Task sleep = new Task("Sleep", 60*120, null, 21, 6, ObjectTypes.BED);
        sleep.addResult(NeedType.ENERGY, 100);
        sleep.addRequiredItem(true, Item.NO_CLOTHES, 1);
        tasks.add(sleep);
        
        Task washHands = new Task("Wash hands", 30, null, 0, 0, ObjectTypes.SINK);
        washHands.addRequiredItem(true, Item.DIRTY_HANDS, 1);
        tasks.add(washHands);
        
        Task useToilet = new Task("Use toilet", 120, null, 0, 0, ObjectTypes.TOILET);
        useToilet.addResult(NeedType.TOILET, 100);
        useToilet.addResultingItem(true, Item.DIRTY_HANDS, 1);
        tasks.add(useToilet);
    }
    
    
    public static ArrayList<Task> getTaskList(){
        if(obj == null) obj = new TaskSingleton();
        return obj.tasks;
    }
}