package se.plushogskolan.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID> {
	
	<S extends T> S save(S entity);

	<S extends T> Iterable<S> save(Iterable<S> entities);
	
	Optional<T> findOne(ID id);
	//T findOne(ID id);
	boolean exists(ID id);

	List<T> findAll();

	List<T> findAll(Iterable<Long> ids);

	long count();

	void delete(ID id);

	void delete(T entity);

}
