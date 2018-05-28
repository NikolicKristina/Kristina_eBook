package com.iktpreobuka.eBook.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.eBook.entities.StudentEntity;

public interface StudentRepository extends CrudRepository<StudentEntity, Integer> {

	
	public StudentEntity findStudentByJmbg(String jmbg);
	
	@Query("select distinct s from StudentEntity s where s.jmbg like ?1%")
	public List<StudentEntity> findStudentByJmbgStartingWith(String jmbg);
	
	public Iterable<StudentEntity> findAll();

}
