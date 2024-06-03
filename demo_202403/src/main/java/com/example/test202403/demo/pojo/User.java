package com.example.test202403.demo.pojo;

import java.util.Date;

public class User {
	private String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	private Date birthday;
	private String sex;
	private String address;
	@Override
	public String toString() {
		return "User [username=" + username + ", birthday=" + birthday + ", sex=" + sex + ", address=" + address
				+ ", getUsername()=" + getUsername() + ", getBirthday()=" + getBirthday() + ", getSex()=" + getSex()
				+ ", getAddress()=" + getAddress() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
}
