/**
 *
 */
package team19.java.DB;

/**
 * Define a Test class to test
 *
 * @author Chu Wu
 */
public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {

        DBManager dbManager = new DBManager();
//		int uid = dbManager.getUserDAO().insertUser("MaMa Ming", "Female", "MISM");
//		dbManager.getRecordDAO().insertRecord(1000100, "Other");

//		dbManager.getUserDAO().deleteUser(-1);
//		dbManager.getRecordDAO().deleteRecord(-1);
//		for(int i=1000901;i<=1001901;i++)
//			dbManager.getUserDAO().deleteUser(i);
        dbManager.getTableManager().displayUsers();
        dbManager.close();

    }

}
