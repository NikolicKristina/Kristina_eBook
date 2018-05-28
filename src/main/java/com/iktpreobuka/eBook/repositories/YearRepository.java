package com.iktpreobuka.eBook.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.eBook.entities.YearEntity;

public interface YearRepository extends CrudRepository<YearEntity, Integer> {

	public YearEntity findYearByYear(Integer year);

	public Page<YearEntity> findAll(Pageable pageable);

}
