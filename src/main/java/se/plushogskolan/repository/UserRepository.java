package se.plushogskolan.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import se.plushogskolan.model.Team;
import se.plushogskolan.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	
	User findByUsername(String username);
	List<User> findByFirstname(String firstname);
	List<User> findByLastname(String lastname);
	List<User> findAllByTeam(Team team);

}
