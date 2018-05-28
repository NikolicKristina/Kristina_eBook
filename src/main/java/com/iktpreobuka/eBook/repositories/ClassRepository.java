package com.iktpreobuka.eBook.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.eBook.entities.ClassEntity;

public interface ClassRepository extends CrudRepository<ClassEntity, Integer> {

	public ClassEntity findClassByClassSign(String classSign);

	public Page<ClassEntity> findAll(Pageable pageable);
	
	@Query("select distinct c from ClassEntity c where c.classSign like ?1%")
	public List<ClassEntity> findClassByClassSignStartingWith(String classSign);

}
