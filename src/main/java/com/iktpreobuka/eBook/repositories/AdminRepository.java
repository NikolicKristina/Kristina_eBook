package com.iktpreobuka.eBook.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.eBook.entities.AdminEntity;

public interface AdminRepository extends CrudRepository<AdminEntity, Integer> {

	public AdminEntity findAdminByJmbg(String jmbg);

	public Page<AdminEntity> findAll(Pageable pageable);

}
