package se.plushogskolan.repository;

import org.springframework.data.repository.CrudRepository;

import se.plushogskolan.model.Team;

public interface TeamRepository extends CrudRepository<Team, Long> {

	
//	@Query("select e from #{#entityName} e where e.name = :name")
//	public Team byName(@Param("name") String xyz);
//	
	public Team findByName(String name);
	
	public Team findOne(Long id);

	
	
	
	
}