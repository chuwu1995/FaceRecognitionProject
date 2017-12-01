/**
 *
 */
package team19.java.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Define a RecordDAO class to implement corresponding SQL, add, delete, modify
 * and return result
 *
 * @author Chu Wu
 */
public class RecordDAO {

    private Statement stmt;

    // Initialize statement
    /**
     * Define RecordDAO method to construct Statement object according to the
     * incoming connection
     *
     * @param con
     */
    public RecordDAO(Connection con) {
        stmt = DBManager.createStmt(con);
    }

    // Get Record objects by user id
    /**
     * Match data set according to the imcoming uid from database query
     *
     * @param uid
     * @return record list that have same uid with param
     */
    public ArrayList<Record> getRecordsByUID(int uid) {
        // sql query
        String query = "SELECT * FROM Records WHERE UID = " + uid;
        return getRecordData(query);
    }
    
    
  //get an Record within a date range. Mia
  	public ArrayList<Record> getRecordByDateRange(String startDate, String endDate) {
  		String query = "SELECT * FROM Records WHERE Date >= '" + startDate + "' AND Date <= '" + endDate + "'";
  		//String query = "SELECT * FROM Records INNER JOIN Users ON Users.UID=Records.UID;";
  		return getRecordData(query);
  	}

    // Get All Record objects 
    /**
     * Get all records from database
     *
     * @return
     */
    public ArrayList<Record> getAllRecords() {
        String query = "SELECT * FROM Records";
        return getRecordData(query);
    }

    /**
     * Get record with the latest date in the given uid recordset
     *
     * @param uid
     * @return record list with special uid and the last date
     */
    public ArrayList<Record> getLastRecordByUID(int uid) {
        // Find record with maximum date and the same record as the given uid
        String query = "SELECT * FROM Records WHERE Date= (SELECT MAX(Date) FROM Records WHERE UID = " + uid + ")";
        return getRecordData(query);
    }

    // Insert an Record to record table
    /**
     * Define insertRecord method with given uid and reason
     *
     * @param uid
     * @param reason
     */
    public void insertRecord(int uid, String reason) {
        // In database, set rid to self-incremented primary key
        // Data is the current time
        try {

            // Insert statement in database, generate date using method in DateController class
            String query = "INSERT INTO Records (UID, Date, Reason) VALUES (" + uid + ", '"
                    + DateController.getCurrentTime() + "', '" + reason + "')";

            stmt.executeUpdate(query);

        } catch (SQLException se) {
            System.out.println(se);
        }
    }

    /**
     * Delete a record
     *
     * @param rid
     */
    public void deleteRecord(int rid) {
        try {

            // Delete statements
            String query = "DELETE FROM RECORDS WHERE RID = " + rid;

            stmt.executeUpdate(query);

        } catch (SQLException se) {
            System.out.println(se);
        }
    }

    // Get Record objects
    private ArrayList<Record> getRecordData(String query) {
        // Create a collection to save the record
        ArrayList<Record> recordList = new ArrayList<Record>();
        Record record = null;

        try {

            // Get database result set
            ResultSet rs = stmt.executeQuery(query);

            // Filter information individually, split attribuites, create objects and add to the result set
            while (rs.next()) {
                int rid = rs.getInt("RID");
                int uid = rs.getInt("UID");
                String date = rs.getString("Date");
                String reason = rs.getString("Reason");

                record = new Record(rid, uid, date, reason);
                recordList.add(record);
            }

            rs.close();

        } catch (SQLException se) {
            System.out.println(se);
        }
        return recordList;
    }

}
