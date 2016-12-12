package se.plushogskolan.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class WorkItem extends BaseEntity {

	private String title;
	private String description;
	private String status;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Issue issue;
	
	protected WorkItem() {}
	
	public WorkItem(String title, String description, String status) {
		this.title = title;
		this.description = description;
		this.status = status;
	}

	public WorkItem(String title, String description) {
		this.title = title;
		this.description = description;
		this.status = WorkItemStatus.Unstarted.toString();
	}

	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}

	public String getStatus() {
		return status;
	}

	public User getUser() {
		return user;
	}

	public Issue getIssue() {
		return issue;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	
}