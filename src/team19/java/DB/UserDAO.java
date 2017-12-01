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
 * Define UserDAO class to perform additions, deletions and check according to
 * corresponding conditions
 *
 * @author Chu Wu
 *
 */
public class UserDAO {

    // initialize statement
    private Statement stmt;

    /**
     * Define a UserDAO method to create Statement object according to input
     *
     * @param con
     */
    public UserDAO(Connection con) {
        stmt = DBManager.createStmt(con);
    }

    /**
     * Insert an user and return the incremented UID.
     *
     * @param name
     * @param gender
     * @param program
     * @return
     */
    public int insertUser(String name, String gender, String program) {
        int uid = -1;
        try {

            // Insert a new record of user
            String insert = "INSERT INTO Users (Name, Gender, Program) VALUES ('" + name
                    + "', '" + gender + "', '" + program + "')";

            stmt.executeUpdate(insert, Statement.RETURN_GENERATED_KEYS);

            // Get generated keys
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                uid = rs.getInt(1);
            }

        } catch (SQLException se) {
            System.out.println(se);
        }
        return uid;
    }

    /**
     * Update user
     *
     * @param colNAME
     * @param value
     * @param uid
     */
    public void updateUser(String colNAME, String value, int uid) {
        try {

            // Update statement
            String insert = "UPDATE Users SET " + colNAME + " = '" + value + "' WHERE UID = " + uid;
            // Execute update
            stmt.executeUpdate(insert);

        } catch (SQLException se) {
            System.out.println(se);
        }
    }

    /**
     * Delete user record according to specified uid
     *
     * @param uid
     */
    public void deleteUser(int uid) {
        try {

            String query = "DELETE FROM USERS WHERE UID = " + uid;

            stmt.executeUpdate(query);
            new DBManager().getRecordDAO().deleteRecord(uid);

        } catch (SQLException se) {
            System.out.println(se);
        }
    }

    // Get one or more User objects in an array list by a given condition
    /**
     * Get all user records that meet the condition
     *
     * @param condition
     * @return users list
     */
    public ArrayList<User> getUserByCondition(String condition) {

        String query = "SELECT * FROM Users WHERE" + condition;

        return getUserData(query);

    }

    // Get one or more User objects in an array list by a given condition
    /**
     * Get all user records with given uid
     *
     * @param uid
     * @return result list
     */
    public ArrayList<User> getUserByUID(int uid) {

        String query = "SELECT * FROM Users WHERE UID = " + uid;
        return getUserData(query);

    }

    // Get all User objects from the User table
    /**
     * Get all user records in database
     *
     * @return all user
     */
    public ArrayList<User> getAllUser() {

        String query = "SELECT * FROM Users";

        return getUserData(query);

    }

    // Get User objects
    private ArrayList<User> getUserData(String query) {
        ArrayList<User> userList = new ArrayList<User>();
        User User = null;

        try {

            ResultSet rs = stmt.executeQuery(query);

            // Parse attributes and create objects and add to result set
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
