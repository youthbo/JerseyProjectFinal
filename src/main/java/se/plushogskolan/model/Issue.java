package se.plushogskolan.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Issue extends BaseEntity{
	
	
	@Column(nullable= false,unique = true)
	private String description;
	
    protected Issue() {}

	public Issue(String description) {		
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
