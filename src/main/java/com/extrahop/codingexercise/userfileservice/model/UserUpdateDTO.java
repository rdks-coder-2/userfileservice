package com.extrahop.codingexercise.userfileservice.model;

import javax.validation.constraints.NotBlank;

public class UserUpdateDTO {
	
	@NotBlank
	private String username;

	private String homeDir;

	private String loginShell;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHomeDir() {
		return homeDir;
	}

	public void setHomeDir(String homeDir) {
		this.homeDir = homeDir;
	}

	public String getLoginShell() {
		return loginShell;
	}

	public void setLoginShell(String loginShell) {
		this.loginShell = loginShell;
	}
}
