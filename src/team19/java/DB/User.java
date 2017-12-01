/**
 *
 */
package team19.java.DB;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Define a User class to save user information
 *
 * @author Chu Wu
 *
 */
public class User {

    private IntegerProperty uid;
    private StringProperty name;
    private StringProperty program;
    private StringProperty gender;

    /**
     * Define a User object and initialize corresponding contributes
     *
     * @param uid
     * @param name
     * @param gender
     * @param program
     */
    public User(int uid, String name, String gender, String program) {

        this.uid = new SimpleIntegerProperty(uid);
        this.name = new SimpleStringProperty(name);
        this.program = new SimpleStringProperty(program);
        this.gender = new SimpleStringProperty(gender);
    }

    /**
     * Define a getter method to get UID
     *
     * @return the uid
     */
    public IntegerProperty getUID() {
        return uid;
    }

    /**
     * Define a getter method to get user name
     *
     * @return the name
     */
    public StringProperty getName() {
        return name;
    }

    /**
     * Define a getter method to get program
     *
     * @return the program
     */
    public StringProperty getProgram() {
        return program;
    }

    /**
     * Define a getter method to get gender
     *
     * @return the gender
     */
    public StringProperty getGender() {
        return gender;
    }

    /**
     * Define a setter method to set name
     *
     * @param name the name to set
     */
    public void setName(StringProperty name) {
        this.name = name;
    }

    /**
     * Define a setter method to set program
     *
     * @param program the program to set
     */
    public void setProgram(StringProperty program) {
        this.program = program;
    }

    /**
     * Define a setter method to set gender
     *
     * @param gender the gender to set
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
