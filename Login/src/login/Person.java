package login;

import java.io.Serializable;

/**
 * <p> This class contains the objects used for the ContactScreen</p>
 * @author Matthew Furlong
 * @author Rj Larsen
 * @see String
 */
public class Person implements Serializable{
	
	private static final long serialVersionUID = 7600071621925696095L;
	private String first_name, last_name, email, address, phone_number;
	
	/**
	 * <p> Sets all necessary fields for a person object in constructor</p>
	 * @param ifirst_name first name of contact
	 * @param ilast_name last name of contact
	 * @param iemail email address of contact
	 * @param iaddress physical address of contact
	 * @param iphone_number phone number
	 */
	public Person(String ifirst_name, String ilast_name, String iemail, String iaddress, String iphone_number) {
		first_name = ifirst_name;
		last_name = ilast_name;
		email = iemail;
		address = iaddress;
		phone_number = iphone_number;
	}
	
	/**
	 * @return phone number
	 */
	public String getNumber() {
		return phone_number;
	}
	
	/**
	 * @return first name
	 */
	public String getFirst() {
		return first_name;
	}
	
	/**
	 * @return last name
	 */
	public String getLast() {
		return last_name;
	}
	
	/**
	 * @return email address
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @return physical address
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * <p> This function sets the first name of the person. Used for editable cells.</p>
	 * @param first Object that must be string
	 */
	public void setFirst(Object first) {
		first_name = (String) first;
	}
	
	/**
	 * <p> This function sets the last name of the person. Used for editable cells.</p>
	 * @param last Object that must be string
	 */
	public void setLast(Object last) {
		last_name = (String) last;
	}
	
	/**
	 * <p> This function sets the email address of the person. Used for editable cells.</p>
	 * @param iemail Object that must be string
	 */
	public void setEmail(Object iemail) {
		email = (String) iemail;
	}
	
	/**
	 * <p> This function sets the physical address of the person. Used for editable cells.</p>
	 * @param iaddress Object that must be string
	 */
	public void setAddress(Object iaddress) {
		address = (String) iaddress;
	}
	
	/**
	 * <p> This function sets the phone number of the person. Used for editable cells.</p>
	 * @param iphone_number Object that must be string
	 */
	public void setNumber(Object iphone_number) {
		phone_number = (String) iphone_number;
	}
	
}
