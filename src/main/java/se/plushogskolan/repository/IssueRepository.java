package se.plushogskolan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import se.plushogskolan.model.Issue;
import se.plushogskolan.model.WorkItem;

public interface IssueRepository extends PagingAndSortingRepository<Issue, Long>{
    
	Issue findByDescription(String description);
    
    @Query("select w from WorkItem w where w.issue =:issue")
	List<WorkItem> findAllByIssue(@Param("issue")Issue issue);
    
}
