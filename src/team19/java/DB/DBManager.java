/**
 *
 */
package team19.java.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Define a DBManager class to create objects to close database connection and
 * save DAO operation
 *
 * @author Chu Wu
 */
public class DBManager {

    private static Connection con = null;

    // Private static final String driver =
    // "org.apache.derby.jdbc.EmbeddedDriver";// edit it to your driver
    private static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String url = "jdbc:derby:FaceRecognitionDB;create=true;user=chuw;password=chuw";// edit
    // it
    // to
    // your
    // own
    // datab

    private UserDAO udao = null;
    private RecordDAO rdao = null;
    private tableManager tm = null;

    // Establish connection
    /**
     * DBManager method to connect, drive and initialize database
     */
    public DBManager() {
        if (!dbExists()) {
            try {
                // Register the drive according to name
                Class.forName(driver);
                // Connect corresponding database according to url
                con = DriverManager.getConnection(url);

            } catch (ClassNotFoundException ce) {
                System.out.println(ce);

            } catch (SQLException se) {
                System.out.println(se);
            }
        }
        // Create UsedDAO, RecordDAO, tableManager object
        udao = new UserDAO(con);
        rdao = new RecordDAO(con);
        tm = new tableManager(con);

    }

    // Create statement
    /**
     * Use database connection to create a Statement object and send sql
     * statement
     *
     * @param con
     * @return Statement object
     */
    public static Statement createStmt(Connection con) {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stmt;
    }

    // Close database
    /**
     * Close connection to database
     */
    public void close() {
        try {
            // Send close command to database
            con = DriverManager.getConnection(url + ";shutdown=true");
        } catch (SQLException se) {
            ; // Do Nothing. System has shut down.
        }
        con = null;
    }

    // Get data access object
    /**
     * Define a getter method
     *
     * @return UsedDAO object
     */
    public UserDAO getUserDAO() {
        return udao;
    }

    // Get data access object
    /**
     * Define a getter method
     *
     * @return RecordDAO object
     */
    public RecordDAO getRecordDAO() {
        return rdao;
    }
    // Get data access object

    /**
     * Define a getter method
     *
     * @return tableManager object
     */
    public tableManager getTableManager() {
        return tm;
    }

    // Test if database exists
    private Boolean dbExists() {
        Boolean exists = false;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url);
            exists = true;
        } catch (Exception e) {
            System.out.println("not exists");
            ; // Do nothing, as DB does not (yet) exist
        }
        return (exists);
    }

}
