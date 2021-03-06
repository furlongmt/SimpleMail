package login;

import java.io.Serializable;
import java.util.ArrayList;


public class Person implements Serializable{
	private String first_name;
	private String last_name;
	private String email;
	private String address;
	private String phone_number;
	
	public Person(String ifirst_name, String ilast_name, String iemail, String iaddress, String iphone_number) {
		first_name = ifirst_name;
		last_name = ilast_name;
		email = iemail;
		address = iaddress;
		phone_number = iphone_number;
	}
	
	public String getNumber() {
		return phone_number;
	}
	
	public String getFirst() {
		return first_name;
	}
	
	public String getLast() {
		return last_name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setFirst(Object first) {
		first_name = (String) first;
	}
	
	public void setLast(Object last) {
		last_name = (String) last;
	}
	
	public void setEmail(Object iemail) {
		email = (String) iemail;
	}
	
	public void setAddress(Object iaddress) {
		address = (String) iaddress;
	}
	
	public void setNumber(Object iphone_number) {
		phone_number = (String) iphone_number;
	}
	
}
