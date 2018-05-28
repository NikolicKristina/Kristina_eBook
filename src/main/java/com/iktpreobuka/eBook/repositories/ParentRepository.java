package com.iktpreobuka.eBook.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.eBook.entities.ParentEntity;

public interface ParentRepository extends CrudRepository<ParentEntity, Integer> {

	public ParentEntity findParentByJmbg(String jmbg);

	public Page<ParentEntity> findAll(Pageable pageable);

	public ParentEntity findParentByDeleted(boolean deleted);
	
	@Query("select distinct p from ParentEntity p where p.jmbg like ?1%")
	public List<ParentEntity> findParentByJmbgStartingWith(String jmbg);

}
