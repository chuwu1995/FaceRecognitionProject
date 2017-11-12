/**
 * 
 */
package team19.java.DB;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Chu Wu
 * 
 */
public class Record {
	private IntegerProperty rid;
	private IntegerProperty uid;
	private StringProperty date;
	private StringProperty reason;

	

	public Record(int rid, int uid, String date, String reason) {

		this.rid = new SimpleIntegerProperty(rid);
		this.uid = new SimpleIntegerProperty(uid);
		this.date = new SimpleStringProperty(date);
		this.reason = new SimpleStringProperty(reason);
	}



	/**
	 * @return the rid
	 */
	public IntegerProperty getrid() {
		return rid;
	}



	/**
	 * @param rid the rid to set
	 */
	public void setrid(IntegerProperty rid) {
		this.rid = rid;
	}



	/**
	 * @return the uid
	 */
	public IntegerProperty getUid() {
		return uid;
	}



	/**
	 * @param uid the uid to set
	 */
	public void setUid(IntegerProperty uid) {
		this.uid = uid;
	}



	/**
	 * @return the date
	 */
	public StringProperty getDate() {
		return date;
	}



	/**
	 * @param date the date to set
	 */
	public void setDate(StringProperty date) {
		this.date = date;
	}



	/**
	 * @return the reason
	 */
	public StringProperty getReason() {
		return reason;
	}



	/**
	 * @param reason the reason to set
	 */
	public void setReason(StringProperty reason) {
		this.reason = reason;
	}
	
}
	
	