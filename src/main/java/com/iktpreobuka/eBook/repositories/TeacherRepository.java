package com.iktpreobuka.eBook.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.eBook.entities.TeacherEntity;

public interface TeacherRepository extends CrudRepository<TeacherEntity, Integer> {

	@Query("select distinct t from TeacherEntity t where t.jmbg like ?1%")
	public List<TeacherEntity> findTeacherByJmbgStartingWith(String jmbg);
	
	public TeacherEntity findTeacherByJmbg(String jmbg);

	public Page<TeacherEntity> findAll(Pageable pageable);
}
