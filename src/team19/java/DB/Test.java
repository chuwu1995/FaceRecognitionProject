/**
 * 
 */
package team19.java.DB;

/**
 * @author Chu Wu
 *  
 */
public class Test {

	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		DBManager dbManager = new DBManager();
//		int uid = dbManager.getUserDAO().insertUser("MaMa Ming", "Female", "MISM");
//		dbManager.getRecordDAO().insertRecord(1000100, "Other");
		
//		dbManager.getUserDAO().deleteUser(1000800);
//		dbManager.getRecordDAO().deleteRecord(1000200);
		
		dbManager.getTableManager().displayUsers();
		
	}
	
}
