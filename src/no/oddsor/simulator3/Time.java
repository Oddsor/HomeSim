
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
    
    public static int getSeconds(double currentTime){
        return (int)(currentTime) % 60;
    }
    
    public static String getNumberFormatted(int number){
        return (number < 10 ? "0" + number: "" + number);
    }
    
    public static int getMinutes(double currentTime){
        return (int)((currentTime) / 60) % 60;
    }
    
    public static int getHours(double currentTime){
        return (int)((currentTime) / (60*60)) % 24;
    }
    
    public static int getDay(double currentTime){
        return 1 + (int) ((currentTime) / (60*60*24)) % 7;
    }
    
    public static String getDayName(double currentTime){
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
    
    public static int getWeek(double currentTime){
        return 1 + ((int) (currentTime) / (60*60*24*7));
    }
}
