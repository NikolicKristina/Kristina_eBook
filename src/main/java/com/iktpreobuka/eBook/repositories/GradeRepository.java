package com.iktpreobuka.eBook.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.eBook.entities.GradeEntity;
import com.iktpreobuka.eBook.enums.GradeType;

public interface GradeRepository extends CrudRepository<GradeEntity, Integer> {

	public Page<GradeEntity> findAll(Pageable pageable);
	
	public GradeEntity findGradeBySemesterAndType(Integer semester, GradeType type);

}
