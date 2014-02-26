/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3.json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import no.oddsor.simulator3.Task;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Odd
 */
public class JSON {
    
    private JSONObject object;
    
    public JSON(String filename) throws Exception{
        JSONParser jp = new JSONParser();
        this.object = (JSONObject)jp.parse(new FileReader("tasks.json"));
        object.
    }
    
    public Collection<Task> getTasks(){
        Collection<Task> tasks = new  ArrayList<>();
        object.
        return null;
    }
}
