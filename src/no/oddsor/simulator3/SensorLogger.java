/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.oddsor.simulator3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SensorLogger {

    public static void main(String[] args) {
        System.out.println(10 / 30 + 1);
        System.out.println(30 / 30 + 1);
        System.out.println(50 / 30 + 1);
        System.out.println(60 / 30 + 1);
        SensorLogger sl;
        try {
            sl = new SensorLogger("yep.txt");
            double time = 1546.5437;
            Random rand = new Random();
            for(int i = 0; i < 200; i++){
                time += (rand.nextInt(30000));
                sl.addSensor(time, "Kitchen sensor", 
                "Motion sensor", "OFF", "eating food");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(SensorLogger.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        
    }
    private BufferedWriter fileWriter;
    private final DecimalFormat df;
    private int counter;
    private int suffix;
    private final String fileName;
    
    public SensorLogger(String fileName) throws IOException{
        try{
            //BufferedReader br = new BufferedReader(new FileReader(fileName));
        }catch(Exception e){
            e.printStackTrace();
        }
        this.fileName = fileName;
        this.suffix = 1;
        this.fileWriter = new BufferedWriter(new FileWriter(fileName+suffix, true));
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df = new DecimalFormat("##.#####", dfs);
        df.setMinimumIntegerDigits(2);
        df.setMaximumIntegerDigits(2);
        df.setMinimumFractionDigits(6);
        df.setMaximumFractionDigits(6);
        System.out.println("How often does this happen?!");
        counter = 0;
    }
    
    public void addSensor(double time, String sensorName, String sensorNote, 
            String sensorValue, String activityName) throws IOException{
        
        int day = Time.getDay(time);
        int dayOfMonth = day % 30;
        int month = day / 30;
        int currentMonth = month % 11 + 1;
        int year = 2014 + (month / 12);
        fileWriter.append(year+ "-" +Time.getNumberFormatted(currentMonth)+"-"+
                Time.getNumberFormatted(dayOfMonth) + " " +
                Time.getNumberFormatted(Time.getHours(time)) + ":" + 
                Time.getNumberFormatted(Time.getMinutes(time)) + ":" + 
                df.format(time % 60)
                +" "+removeSpaces(sensorName) + " " + removeSpaces(sensorNote)
                + " " + removeSpaces(sensorValue) + " " + 
                removeSpaces(activityName) + "\n");
        fileWriter.flush();
        if(counter == 10000){
            counter = 0;
            suffix ++;
            fileWriter.close();
            this.fileWriter = new BufferedWriter(
                    new FileWriter(fileName+suffix, true));
        }counter++;
    }
    public String removeSpaces(String text){
        return text.replace(' ', '_');
    }
    
}
