package com.milstein.japi.services.v1;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class UserHolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "id")
	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
