
package no.oddsor.simulator3;

import java.util.ArrayList;

/**
 *
 * @author Odd
 */
public class TaskSingleton {
    
    private static TaskSingleton obj;
    
    ArrayList<Task> tasks;
    
    private TaskSingleton(){
        tasks = new ArrayList<>();
        
        Task getIngredients = new Task("Get ingredients from fridge", 10);
        getIngredients.addResultingItem(Item.RAW_FOOD);
        tasks.add(getIngredients);
        
        Task cookFood = new Task("Cook food on oven", 200);
        cookFood.addRequiredItem(Item.RAW_FOOD);
        cookFood.addResultingItem(Item.COOKED_FOOD);
        tasks.add(new Task("Cook food", 240));
        
        Task eatDinner = new Task("Eat cooked food", 100);
        eatDinner.addRequiredItem(Item.COOKED_FOOD);
        tasks.add(eatDinner);
    }
    
    
    public static ArrayList<Task> getTaskList(){
        if(obj == null) obj = new TaskSingleton();
        return obj.tasks;
    }
}
