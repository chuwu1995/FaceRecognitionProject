/**
 * 
 */
package team19.java.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Chu Wu
 * 
 */
public class DBManager {

	private static Connection con = null;

	 private static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";// edit it to your driver
	// private static final String driver = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:derby://localhost:1527/JavaProject";// edit
																						// it																					// database
	String username = "Mia";// database username
	String password = "12345";// database password

	private UserDAO udao = null;
	private RecordDAO rdao = null;

	// establish connection
	public DBManager() {
		if (!dbExists()) {
			try {
				Class.forName(driver);

				con = DriverManager.getConnection(url, username, password);

			} catch (ClassNotFoundException ce) {
				System.out.println(ce);

			} catch (SQLException se) {
				System.out.println(se);
			}
		}
		
		udao = new UserDAO(con);
		rdao = new RecordDAO(con);

	}

	// create statement
	public static Statement createStmt(Connection con) {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stmt;
	}

	// close database
	public void close() {
		try {
			con = DriverManager.getConnection(url + ";shutdown=true");
		} catch (SQLException se) {
			; // Do Nothing. System has shut down.
		}
		con = null;
	}

	// get data access object
	public UserDAO getUserDAO() {
		return udao;
	}

	// get data access object
	public RecordDAO getRecordDAO() {
		return rdao;
	}

	// test if database exists
	private Boolean dbExists() {
		Boolean exists = false;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			exists = true;
		} catch (Exception e) {
			System.out.println("not exists");; // Do nothing, as DB does not (yet) exist
		}
		return (exists);
	}

}