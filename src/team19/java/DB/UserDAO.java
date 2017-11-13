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

			String insert = "INSERT INTO `Users` ( `Name`, `Gender`, `Program`) VALUES ('" + name
					+ "', '" + gender + "', '" + program + "')";
			String query = "SELECT MAX(UID) FROM `Users`";
			stmt.executeUpdate(insert);
			ResultSet rs = stmt.executeQuery(query);	
			if(rs.next()) {
				uid = rs.getInt("MAX(UID)");
			}

		} catch (SQLException se) {
			System.out.println(se);
		}
		return uid;
	}

	// get one or more User objects in an array list by a given condition
	public ArrayList<User> getUserByCondition(String condition) {

		String query = "SELECT * FROM `Users` WHERE" + condition;

		return getUserData(query);

	}

	// get all User objects from the User table
	public ArrayList<User> getAllUser() {

		String query = "SELECT * FROM `Users`";

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
