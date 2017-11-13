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
 * @author Chu Wu
 * 
 */
public class RecordDAO {
	private Statement stmt;

	// initialize statement
	public RecordDAO(Connection con) {
		stmt = DBManager.createStmt(con);
	}

	// get Record objects by user id
	public ArrayList<Record> getRecordsByUID(int uid) {
		String query = "SELECT * FROM Records WHERE UID = " + uid;
		return getRecordData(query);
	}

	// get All Record objects 
	public ArrayList<Record> getAllRecords() {
		String query = "SELECT * FROM Records";
		return getRecordData(query);
	}
	
	public ArrayList<Record> getLastRecordByUID(int uid) {
		String query = "SELECT * FROM Records WHERE Date= (SELECT MAX(Date) FROM Record WHERE UID = " + uid + ")";
		return getRecordData(query);
	}

	// insert an Record to record table
	public void insertRecord(int uid, String reason) {
		// in database, set rid to self-incremented primary key
		// data is the current time
		try {

			String query = "INSERT INTO Records (UID, Date, Reason) VALUES (" + uid + ", '"
					+ DateController.getCurrentTime() + "', '" + reason + "')";

			stmt.executeUpdate(query);

		} catch (SQLException se) {
			System.out.println(se);
		}
	}
	
	/**
	 * delete a record
	 * @param rid
	 */
	public void deleteRecord(int rid){
		try {

			String query = "DELETE FROM RECORDS WHERE RID = " + rid;

			stmt.executeUpdate(query);

		} catch (SQLException se) {
			System.out.println(se);
		}
	}

	// get Record objects
	private ArrayList<Record> getRecordData(String query) {
		ArrayList<Record> recordList = new ArrayList<Record>();
		Record record = null;

		try {

			ResultSet rs = stmt.executeQuery(query);

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
