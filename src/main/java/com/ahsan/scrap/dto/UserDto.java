package com.ahsan.scrap.dto;

import com.ahsan.scrap.model.UserDtls;

public class UserDto {
	private UserDtls userDtls;
	private int hasAmount;

	public UserDto(UserDtls userDtls, int hasAmount) {
		this.userDtls = userDtls;
		this.hasAmount = hasAmount;
	}

	public UserDtls getUserDtls() {
		return userDtls;
	}

	public void setUserDtls(UserDtls userDtls) {
		this.userDtls = userDtls;
	}

	public int getHasAmount() {
		return hasAmount;
	}

	public void setHasAmount(int hasAmount) {
		this.hasAmount = hasAmount;
	}
}
