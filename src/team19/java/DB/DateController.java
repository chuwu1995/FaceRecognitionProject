/**
 *
 */
package team19.java.DB;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Define a DateController class to generate the date using corresponding format
 * according to current time
 *
 * @author Chu Wu
 */
public class DateController {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Define a getCurrentTime method, using agreed format (yyyy-MM-dd HH: mm:
     * ss)
     *
     * @return return the current system time, date String
     */
    public static String getCurrentTime() {
        Date date = new Date();
        return dateFormat.format(date);
    }

}
