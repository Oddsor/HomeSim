
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
        int[] notWeekends = {6, 7};
        int[] notSundays = {7};
        
        tasks = new ArrayList<>();
        Task getIngredients = new Task("Get ingredients from fridge", 10, null, 
                0, 0, Task.FLOAT_THROUGH, 
                ObjectTypes.REFRIDGERATOR);
        getIngredients.addResultingItem(true, Item.RAW_FOOD, 1);
        getIngredients.addRequiredItem(false, Item.RAW_FOOD, 1);
        tasks.add(getIngredients);
        
        Task cookFood = new Task("Cook food on oven", 60*5, null, 15, 19, 
                Task.FLOAT_THROUGH, ObjectTypes.OVEN);
        cookFood.addRequiredItem(true, Item.RAW_FOOD, 1);
        cookFood.addResultingItem(false, Item.COOKED_FOOD, 1);
        tasks.add(cookFood);
        
        Task eatDinner = new Task("Eat dinner", 60*5, null, 15, 19, 
                Task.BREAK_POINT, ObjectTypes.CHAIR);
        eatDinner.addRequiredItem(true, Item.COOKED_FOOD, 1);
        eatDinner.addResult(NeedType.HUNGER, 100);
        tasks.add(eatDinner);
        
        Task sleep = new Task("Sleep", 60*120, null, 21, 6, 
                Task.BREAK_POINT, ObjectTypes.BED);
        sleep.addResult(NeedType.ENERGY, 100);
        sleep.addRequiredItem(true, Item.NO_CLOTHES, 1);
        tasks.add(sleep);
        
        Task washHands = new Task("Wash hands", 30, null, 0, 0, 
                Task.FLOAT_THROUGH, ObjectTypes.SINK);
        washHands.addRequiredItem(true, Item.DIRTY_HANDS, 1);
        tasks.add(washHands);
        
        Task useToilet = new Task("Use toilet", 120, null, 0, 0, 
                Task.BREAK_POINT, ObjectTypes.TOILET);
        useToilet.addResult(NeedType.TOILET, 100);
        useToilet.addResultingItem(true, Item.DIRTY_HANDS, 1);
        tasks.add(useToilet);
        
        Task shower = new Task("Take a shower", 120, null, 0, 0, true, ObjectTypes.SHOWER);
        shower.addResult(NeedType.HYGIENE, 100);
        tasks.add(shower);
        
        Task buyFood = new Task("Buy food", 600, notSundays, 9, 22, Task.BREAK_POINT, ObjectTypes.OUTSIDE);
        buyFood.addResultingItem(true, Item.RAW_FOOD, 5);
        tasks.add(buyFood);
        
        Task foodToFridge = new Task("Store food", 10, null, 0, 0, Task.FLOAT_THROUGH, ObjectTypes.REFRIDGERATOR);
        foodToFridge.addResultingItem(false, Item.RAW_FOOD, 5);
        foodToFridge.addRequiredItem(true, Item.RAW_FOOD, 5);
        tasks.add(foodToFridge);
        
        Task undress = new Task("Put clothes in washing machine", 10, null, 0, 0, 
            Task.FLOAT_THROUGH, ObjectTypes.WASHING_MACHINE);
        undress.addResultingItem(true, Item.NO_CLOTHES, 1);
        tasks.add(undress);
    }
    
    
    public static ArrayList<Task> getTaskList(){
        if(obj == null) obj = new TaskSingleton();
        return obj.tasks;
    }
}