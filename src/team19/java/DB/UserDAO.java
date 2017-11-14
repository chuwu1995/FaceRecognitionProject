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
public class UserDAO {
	private Statement stmt;


	// initialize statement
	public UserDAO(Connection con) {
		stmt = DBManager.createStmt(con);
	}

	/**
	 * insert an user and return the incremented UID.
	 * @param name
	 * @param gender
	 * @param program
	 * @return
	 */
	public int insertUser(String name, String gender, String program) {
		int uid = -1;
		try {

			String insert = "INSERT INTO Users (Name, Gender, Program) VALUES ('" + name
					+ "', '" + gender + "', '" + program + "')";
		
			stmt.executeUpdate(insert,Statement.RETURN_GENERATED_KEYS);
		
			ResultSet rs = stmt.getGeneratedKeys();	
			if(rs.next())
				uid = rs.getInt(1);
			

		} catch (SQLException se) {
			System.out.println(se);
		}
		return uid;
	}
	
	/**
	 * update a cell
	 * @param col
	 * @param value
	 * @param uid
	 */
	public void updateUser(String colNAME, String value, int uid) {
		try {

			String insert = "UPDATE Users SET " + colNAME + " = '" + value + "' WHERE UID = " + uid;
			stmt.executeUpdate(insert);

		} catch (SQLException se) {
			System.out.println(se);
		}
	}
	
	
	public void deleteUser(int uid){
		try {

			String query = "DELETE FROM USERS WHERE UID = " + uid;

			stmt.executeUpdate(query);
			new DBManager().getRecordDAO().deleteRecord(uid);

		} catch (SQLException se) {
			System.out.println(se);
		}
	}

	// get one or more User objects in an array list by a given condition
	public ArrayList<User> getUserByCondition(String condition) {

		String query = "SELECT * FROM Users WHERE" + condition;

		return getUserData(query);

	}

	// get all User objects from the User table
	public ArrayList<User> getAllUser() {

		String query = "SELECT * FROM Users";

		return getUserData(query);

	}

	// get User objects
	private ArrayList<User> getUserData(String query) {
		ArrayList<User> userList = new ArrayList<User>();
		User User = null;

		try {

			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				int uid = rs.getInt("UID");
				String name = rs.getString("Name");
				String gender = rs.getString("Gender");
				String program = rs.getString("Program");

				User = new User(uid, name, gender, program);
				userList.add(User);
			}

			rs.close();

		} catch (SQLException se) {
			System.out.println(se);
		}
		return userList;
	}
	
	
	
	 
	

}
