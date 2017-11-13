/**
 * 
 */
package team19.java.DB;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Chu Wu
 *  
 */
public class DateController {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String getCurrentTime(){
		Date date = new Date();
		return dateFormat.format(date);
	}


}
