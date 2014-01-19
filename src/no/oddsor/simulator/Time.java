
package no.oddsor.simulator;

/**
 *
 * @author Odd
 */
public class Time {
    
    
    long startTime;
    
    /**
     * Input time in seconds.
     * @param startTime Current time in seconds
     */
    public Time(long startTime){
        this.startTime = startTime;
    }
    
    public int getSeconds(long currentTime){
        return (int)(currentTime - startTime) % 60;
    }
    
    public String getNumberFormatted(int number){
        return (number < 10 ? "0" + number: "" + number);
    }
    
    public int getMinutes(long currentTime){
        return (int)((currentTime - startTime) / 60) % 60;
    }
    
    public int getHours(long currentTime){
        return (int)((currentTime - startTime) / (60*60)) % 24;
    }
    
    public int getDay(long currentTime){
        return 1 + (int) ((currentTime - startTime) / (60*60*24)) % 7;
    }
    
    public String getDayName(long currentTime){
        String dayName = "";
        int day = getDay(currentTime);
        switch(day){
            case 7: dayName = "Sunday";
                break;
            case 6: dayName = "Saturday";
                break;
            case 5: dayName = "Friday";
                break;
            case 4: dayName = "Thursday";
                break;
            case 3: dayName = "Wednesday";
                break;
            case 2: dayName = "Tuesday";
                break;
            case 1: dayName = "Monday";
                break;
        }
        return dayName;
    }
    
    public int getWeek(long currentTime){
        return 1 + ((int) (currentTime - startTime) / (60*60*24*7));
    }
}
