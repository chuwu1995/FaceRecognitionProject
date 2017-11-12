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
		
		User user = new User(1111, "Chu Wu", "male", "mism");
		Record r = new Record(1111, 1111, DateController.getCurrentTime(), "eat");

		String name = user.getName().getValue();
		String date = r.getDate().getValue();
		
		System.out.println(name + " visit the reception at " + date);
		
		DBManager dbManager = new DBManager();
		ArrayList<User> ul = dbManager.getUserDAO().getAllUser();
		
		for(User u: ul){
			System.out.println(u.getGender().getValue());
			ArrayList<Record> rl = dbManager.getRecordDAO().getRecordsByUID(u.getUid().getValue());
			for(Record record:rl)
				System.out.println(record.getDate().getValue());
		}
	}

}
