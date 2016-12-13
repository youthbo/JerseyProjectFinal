package se.plushogskolan.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import se.plushogskolan.model.Status;
import se.plushogskolan.model.User;
import se.plushogskolan.model.WorkItem;
import se.plushogskolan.model.WorkItemStatus;
import se.plushogskolan.repository.WorkItemRepository;

@Service
public class WorkItemService {
	@Autowired
	private WorkItemRepository workItemRepository;

	public WorkItemService(){}
	
	public WorkItemService(WorkItemRepository workItemRepository) {
		this.workItemRepository = workItemRepository;
	}

	public List<WorkItem> findAllByTeamName(String name){
		return workItemRepository.findAllByTeamName(name);
	}
	
	
	public List<WorkItem> findAllByStatus(WorkItemStatus status){
		return this.workItemRepository.findAllByStatus(status.toString());
	}

	
	public List<WorkItem> findAllByUser(Long userId){
		return this.workItemRepository.findAllByUserId(userId);
	}

	
	public List<WorkItem> findAllByUser(User user){
		return this.workItemRepository.findAllByUser(user);
	}
	
	@Transactional
	public WorkItem create(WorkItem workItem){
		return	this.workItemRepository.save(workItem);
	}
	
	@Transactional
	public WorkItem updateStatus(Long id, WorkItemStatus status){
		WorkItem workItem=workItemRepository.findOne(id);
		if(workItem==null)
			throw new ServiceException("Update status failed. WorkItem with id:"+id+" is null");
		workItem.setStatus(status.toString());
		workItemRepository.save(workItem); 
		return workItem;
	}

	@Transactional
	public WorkItem delete(Long id){
		WorkItem workItem=workItemRepository.findOne(id);
		if(workItem!=null)
			workItemRepository.delete(workItem);
		return workItem;
		
	}
	
	public WorkItem delete(WorkItem workItem){
		return delete(workItem.getId());
	}
	
	public List<WorkItem> findByTitleContaining(String text){
		return workItemRepository.findByTitleContaining(text);
	}
	
	public List<WorkItem> findByDescriptionContaining(String text){
		return workItemRepository.findByDescriptionContaining(text);
	}

	@Transactional
	public void addWorkItemToUser(WorkItem workItem, User user){
		if( user.getStatus().equals(Status.ACTIVE.toString()) && checkNumberofWorkItems(user)){
			workItem.setUser(user);
			this.workItemRepository.save(workItem);
		}
		else{
			throw new ServiceException("Can not add a user to work item. User is not active or already has more than 5 work items.");
		}
	}

	/**
	 * This method checks to see if there are 5 work items or less assigned to a user.
	 * @param user
	 * @return
     */
	public boolean checkNumberofWorkItems(User user){
		return this.workItemRepository.findAllByUser(user).size() < 5;
	}
	
	public List<WorkItem> findAllWorkItems(int page,int amount){
		Pageable pageable = new PageRequest(page,amount,Sort.Direction.DESC, "updatedDate");
		return workItemRepository.findAll(pageable).getContent();
	}
	
//	public List<WorkItem> findFinishedWorkItem(ZonedDateTime start,ZonedDateTime end){
//		Pageable pageable = new PageRequest(0,10,Sort.Direction.DESC,"updatedDate");
//		return workItemRepository.findFinishedWorkItem(start, end,pageable).getContent();
//	}
}
