
package no.oddsor.simulator3;

/**
 *
 * @author Odd
 */
public class Time {
    double startTime;
    
    /**
     * Input time in seconds.
     * @param startTime Current time in seconds
     */
    public Time(double startTime){
        this.startTime = startTime;
    }
    
    public int getSeconds(double currentTime){
        return (int)(currentTime - startTime) % 60;
    }
    
    public String getNumberFormatted(int number){
        return (number < 10 ? "0" + number: "" + number);
    }
    
    public int getMinutes(double currentTime){
        return (int)((currentTime - startTime) / 60) % 60;
    }
    
    public int getHours(double currentTime){
        return (int)((currentTime - startTime) / (60*60)) % 24;
    }
    
    public int getDay(double currentTime){
        return 1 + (int) ((currentTime - startTime) / (60*60*24)) % 7;
    }
    
    public String getDayName(double currentTime){
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
    
    public int getWeek(double currentTime){
        return 1 + ((int) (currentTime - startTime) / (60*60*24*7));
    }
}
