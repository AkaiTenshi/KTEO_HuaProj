package Beans;

import java.io.Serializable;

public class User implements Serializable{

	//Variable initialization
	private static final long serialVersionUID = 1L;
	private String username;
	private String fullname;
	private String email;
	private int id;
	private String level;
	
	//Constructor
	public User(String username, String fullname, String email, String level, int id){
		this.fullname = fullname;
		this.username = username;
		this.email = email;
		this.id = id;
		this.level=level;
	}
	

	// Accessors & Mutators
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getFullname() {
		return fullname;
	}


	public void setFullname(String fullname) {
		this.fullname = fullname;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
	}


	@Override
	public String toString() {
		return this.fullname + "(" + this.level + ")";
	}
	

}
