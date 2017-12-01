/**
 *
 */
package team19.java.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Manage table of the database
 *
 * @author Chu Wu
 */
public class tableManager {

    Statement stmt = null;
    // initialize statement

    /**
     * Constructor and create a Statement object
     *
     * @param con
     */
    public tableManager(Connection con) {
        stmt = DBManager.createStmt(con);
    }

    /**
     * Use method in RecordDAO class and display the records
     */
    public void displayRecords() {
        DBManager dbManager = new DBManager();
        ArrayList<Record> rl = dbManager.getRecordDAO().getAllRecords();
        // Traverse and output records
        for (Record r : rl) {
            System.out.println(r.toString());
        }
    }

    /**
     * Use method in UserDAO class and display the records
     */
    public void displayUsers() {
        DBManager dbManager = new DBManager();

        ArrayList<User> ul = dbManager.getUserDAO().getAllUser();

        // Output all the users and related records
        for (User u : ul) {
            System.out.println(u.toString());
            ArrayList<Record> rl = dbManager.getRecordDAO().getRecordsByUID(u.getUID().getValue());
            for (Record record : rl) {
                System.out.println("  " + record.toString());
            }
        }
    }

    /**
     * Define dropAndRecreateTables method to delete old tables and recreate
     * tables
     *
     * @throws SQLException
     */
    public void dropAndRecreateTables() throws SQLException {
        drop("Users");
        drop("Records");
        createUserTable();
        createRecordTable();
    }

    /**
     * Delete selected tables
     *
     * @param table
     * @throws SQLException
     */
    public void drop(String table) throws SQLException {
        // Using sql statements to delete tables
        String drop = "DROP TABLE " + table;
        stmt.executeUpdate(drop);
    }

    /**
     * Define createUserTalbe method to create user table
     *
     * @throws SQLException
     */
    public void createUserTable() throws SQLException {
        // Using sql statements to create a new table
        String create = "CREATE TABLE USERS (UID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1000000, INCREMENT BY 1),Name varchar(30),Gender varchar(15),Program varchar(15),CONSTRAINT primary_key PRIMARY KEY (UID))";
        stmt.executeUpdate(create);
    }

    /**
     * Define createRecordTable to create a record table
     *
     * @throws SQLException
     */
    public void createRecordTable() throws SQLException {
        String create = "CREATE TABLE RECORDS (RID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1000000, INCREMENT BY 1),UID INTEGER,Date varchar(20),Reason varchar(30),CONSTRAINT primary_key_RID PRIMARY KEY (RID))";
        stmt.executeUpdate(create);

    }

}
