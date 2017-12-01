/**
 *
 */
package team19.java.DB;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * Define a Record class to storage properties of the object
 *
 * @author Chu Wu
 */
public class Record {

    // Four properties of Record object
    private IntegerProperty rid;
    private IntegerProperty uid;
    private StringProperty date;
    private StringProperty reason;

    /**
     * Construct Record method and initialize rid
     *
     * @param rid
     * @param uid
     * @param date
     * @param reason
     */
    public Record(int rid, int uid, String date, String reason) {

        // Create corresponding attribute according to input value
        this.rid = new SimpleIntegerProperty(rid);
        this.uid = new SimpleIntegerProperty(uid);
        this.date = new SimpleStringProperty(date);
        this.reason = new SimpleStringProperty(reason);
    }

    /**
     * Define a getter method to get rid
     *
     * @return the rid
     */
    public IntegerProperty getrid() {
        return rid;
    }

    /**
     * Define a setter method to set rid
     *
     * @param rid the rid to set
     */
    public void setrid(IntegerProperty rid) {
        this.rid = rid;
    }

    /**
     * Define a getter method to get uid
     *
     * @return the uid
     */
    public IntegerProperty getUid() {
        return uid;
    }

    /**
     * Define a setter method to set uid
     *
     * @param uid the uid to set
     */
    public void setUid(IntegerProperty uid) {
        this.uid = uid;
    }

    /**
     * Define a getter method to get date
     *
     * @return the date
     */
    public StringProperty getDate() {
        return date;
    }

    /**
     * Define a setter method to set date
     *
     * @param date the date to set
     */
    public void setDate(StringProperty date) {
        this.date = date;
    }

    /**
     * Define a getter method to get reason
     *
     * @return the reason
     */
    public StringProperty getReason() {
        return reason;
    }

    /**
     * Define a setter method to set reason
     *
     * @param reason the reason to set
     */
    public void setReason(StringProperty reason) {
        this.reason = reason;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Record [rid=" + rid.getValue() + ", uid=" + uid.getValue() + ", date=" + date.getValue() + ", reason=" + reason.getValue() + "]";
    }

}
