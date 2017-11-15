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
	public static void main(String[] args) {
	
		DBManager dbManager = new DBManager();
//		int uid = dbManager.getUserDAO().insertUser("MaMa Ming", "Female", "MISM");
//		dbManager.getRecordDAO().insertRecord(1000100, "Other");
		
//		dbManager.getUserDAO().deleteUser(1000800);
//		dbManager.getRecordDAO().deleteRecord(1000200);
//		for(int i=1000901;i<=1001901;i++)
//			dbManager.getUserDAO().deleteUser(1002000);
//			dbManager.getUserDAO().deleteUser(1000100);

		dbManager.getTableManager().displayUsers();
		dbManager.close();
		
	}
	
}
//User [uid=1000000, name=Chu Wu, program=MSIT, gender=Male]
//		  Record [rid=1000000, uid=1000000, date=2017-11-11 12:12:12, reason=Other]
//		  Record [rid=1001500, uid=1000000, date=2017-11-15 22:15:58, reason=Meet With A Person]
//		  Record [rid=1001400, uid=1000000, date=2017-11-15 20:24:08, reason=Retreive Something]
//		User [uid=1002200, name=test, program=MISM, gender=Male]
//		  Record [rid=1001502, uid=1002200, date=2017-11-15 22:19:58, reason=Lost Registration]
//		User [uid=1002300, name=tiancai, program=MSPPM, gender=Male]
//		User [uid=1002400, name=XIAOZONG, program=MISM, gender=Female]
//		  Record [rid=1001501, uid=1002400, date=2017-11-15 22:18:15, reason=Retreive Something]
//		User [uid=1002401, name=XXD, program=MISM, gender=Male]