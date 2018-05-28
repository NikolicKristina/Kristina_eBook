package com.iktpreobuka.eBook.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.iktpreobuka.eBook.entities.SubjectEntity;

public interface SubjectRepository
		extends CrudRepository<SubjectEntity, Integer>, PagingAndSortingRepository<SubjectEntity, Integer> {


	public SubjectEntity findSubjectByNameIgnoreCase(String name);
	
	@Query("select distinct s from SubjectEntity s where s.name like ?1%")
	public List<SubjectEntity> findSubjectByNameStartingWith(String name);

}
