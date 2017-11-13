/**
 * 
 */
package team19.java.DB;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Chu Wu
 * 
 */
public class User {

	private IntegerProperty uid;
	private StringProperty name;
	private StringProperty program;
	private StringProperty gender;

	public User(int uid, String name, String gender, String program) {

		this.uid = new SimpleIntegerProperty(uid);
		this.name = new SimpleStringProperty(name);
		this.program = new SimpleStringProperty(program);
		this.gender = new SimpleStringProperty(gender);
	}

	/**
	 * @return the uid
	 */
	public IntegerProperty getUID() {
		return uid;
	}

	/**
	 * @return the name
	 */
	public StringProperty getName() {
		return name;
	}

	/**
	 * @return the program
	 */
	public StringProperty getProgram() {
		return program;
	}

	/**
	 * @return the gender
	 */
	public StringProperty getGender() {
		return gender;
	}



	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(StringProperty name) {
		this.name = name;
	}

	/**
	 * @param program
	 *            the program to set
	 */
	public void setProgram(StringProperty program) {
		this.program = program;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(StringProperty gender) {
		this.gender = gender;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [uid=" + uid.getValue() + ", name=" + name.getValue() + ", program=" + program.getValue() + ", gender=" + gender.getValue() + "]";
	}
	
	

}
