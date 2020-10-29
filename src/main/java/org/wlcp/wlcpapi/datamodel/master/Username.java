package org.wlcp.wlcpapi.datamodel.master;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: Username
 *
 */
@Entity
@Table(name = "USERNAME")
public class Username implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(length = 40, name = "USERNAME_ID")
	private String usernameId;
	
	@Column(length = 60, name = "PASSWORD")
	private String password;
	
	@Column(length = 40, name = "FIRST_NAME")
	private String firstName;
	
	@Column(length = 40, name = "LAST_NAME")
	private String lastName;
	
	@Column(length = 40, name = "EMAIL_ADDRESS")
	private String emailAddress;

	public Username() {
		super();
	}

	public Username(String usernameId, String password, String firstName, String lastName, String emailAddress) {
		super();
		this.usernameId = usernameId;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
	}

	public String getUsernameId() {
		return usernameId;
	}

	public void setUsernameId(String usernameId) {
		this.usernameId = usernameId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
   
}
