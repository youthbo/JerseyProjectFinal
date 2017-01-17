package se.plushogskolan.jersey.model;

import java.util.UUID;

public class Token {

	private String access_token;
	private String expiration_time;
	
	public Token() {
		this.access_token = generateToken();
		this.expiration_time = new Long(System.currentTimeMillis()+600*1000).toString();
	}

	private String generateToken() {
		return UUID.randomUUID().toString();
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getExpiration_time() {
		return expiration_time;
	}

	public void setExpiration_time(String expiration_time) {
		this.expiration_time = expiration_time;
	}
	
	
	
}
