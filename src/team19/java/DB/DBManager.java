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

	// private static final String driver =
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
	
	// establish connection
	public DBManager() {
		if (!dbExists()) {
			try {
				Class.forName(driver);

				con = DriverManager.getConnection(url);

			} catch (ClassNotFoundException ce) {
				System.out.println(ce);

			} catch (SQLException se) {
				System.out.println(se);
			}
		}

		udao = new UserDAO(con);
		rdao = new RecordDAO(con);
		tm = new tableManager(con);

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
	// get data access object
		public tableManager getTableManager() {
			return tm;
		}

	// test if database exists
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