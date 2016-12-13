package se.plushogskolan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import se.plushogskolan.model.User;
import se.plushogskolan.model.WorkItem;

public interface WorkItemRepository extends PagingAndSortingRepository<WorkItem, Long> {

	List<WorkItem> findAllByStatus(String status);

	List<WorkItem> findAllByUserId(long userId);

	List<WorkItem> findAllByUser(User user);

	List<WorkItem> findByDescriptionContaining(String text);

	List<WorkItem> findByTitleContaining(String text);

	@Query("select w from WorkItem w where w.user.team.name=:teamName")
	List<WorkItem> findAllByTeamName(@Param("teamName") String teamName);

}
