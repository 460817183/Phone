package com.example.goviewer;

public class Person {
	
	private int id;
	
	private String account;
	private String password;
	
	
	
	public Person() {
		super();
	}
	
	public Person(String account, String password) {
		super();
		this.account = account;
		this.password = password;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
