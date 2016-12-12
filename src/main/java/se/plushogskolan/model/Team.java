package se.plushogskolan.model;



import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Team extends BaseEntity {

	
	@Column(unique = true)
	private String name;
	
	private String status;
	
	
	protected Team() {
	}

	public Team(String name) {
		this.name = name;
		this.status = Status.ACTIVE.toString();
	}

	

	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setName(String name) {
		this.name = name;
	}


}
