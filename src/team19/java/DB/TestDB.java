/**
 * 
 */
package team19.java.DB;

import java.util.ArrayList;

/**
 * @author Chu Wu
 * 
 */
public class TestDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// test two model class: user & record
		// value is wrapped in JavaFX property object. So you need to use
		// getValue method to access value.
		

		
		DBManager dbManager = new DBManager();
                dbManager.getUserDAO().insertUser(1111111, "ChuWu", "male", "mism");
//		ArrayList<User> ul = dbManager.getUserDAO().getAllUser();
// 
//		//dbManager.getRecordDAO().insertRecord(0, date);
//		for(User u: ul){
//			System.out.println(u.getGender().getValue());
//			ArrayList<Record> rl = dbManager.getRecordDAO().getRecordsByUID(u.getUid().getValue());
//			for(Record record:rl)
//				System.out.println(record.getDate().getValue());
//		}
	}

}
