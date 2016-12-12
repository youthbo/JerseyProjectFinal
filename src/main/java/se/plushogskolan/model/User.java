package se.plushogskolan.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public final class User extends BaseEntity {
	
	private String firstname;
	private String lastname;
	private String status;
	
	@Column(unique = true)
	private String username;
	
	@ManyToOne
	private Team team;
	
	public User() {}
	
	public User(String firstname, String lastname, String username, Team team) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.team = team;
		this.status = Status.ACTIVE.toString();
	}


	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getUsername() {
		return username;
	}

	public Team getTeam() {
		return team;
	}

	public String getStatus() {
		return status;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
	public void setStatus(Status userStatus) {
		this.status = userStatus.toString();
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

}