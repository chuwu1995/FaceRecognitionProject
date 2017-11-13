/**
 * 
 */
package team19.java.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author Chu Wu
 * 
 */
public class tableManager {
	Statement stmt = null;
			// initialize statement
			public tableManager(Connection con) {
				stmt = DBManager.createStmt(con);
			}
	
	public void displayRecords(){
		DBManager dbManager = new DBManager();
		ArrayList<Record> rl = dbManager.getRecordDAO().getAllRecords();
		for(Record r:rl)
			System.out.println(r.toString());
	}
	
	public void displayUsers(){
		DBManager dbManager = new DBManager();

		ArrayList<User> ul = dbManager.getUserDAO().getAllUser();
		
		for(User u: ul){
			System.out.println(u.toString());
			ArrayList<Record> rl = dbManager.getRecordDAO().getRecordsByUID(u.getUID().getValue());
			for(Record record:rl)
				System.out.println("  "+record.toString());
		}
	}
	
	public void dropAndRecreateTables() throws SQLException{
		drop("Users");
		drop("Records");
		createUserTable();
		createRecordTable();
	}
	public void drop(String table) throws SQLException{
		 String drop = "DROP TABLE "+table;
		 stmt.executeUpdate(drop);
	}
	
	public void createUserTable() throws SQLException{
		String create = "CREATE TABLE USERS (UID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1000000, INCREMENT BY 1),Name varchar(30),Gender varchar(15),Program varchar(15),CONSTRAINT primary_key PRIMARY KEY (UID))";
		stmt.executeUpdate(create);
	}
	public void createRecordTable() throws SQLException{
		String create = "CREATE TABLE RECORDS (RID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1000000, INCREMENT BY 1),UID INTEGER,Date varchar(20),Reason varchar(30),CONSTRAINT primary_key_RID PRIMARY KEY (RID))";
		stmt.executeUpdate(create);

	}

}
