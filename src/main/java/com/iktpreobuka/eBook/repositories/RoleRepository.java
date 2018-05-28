package com.iktpreobuka.eBook.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.eBook.entities.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {

	public RoleEntity findByRoleName(String roleName);

	public Page<RoleEntity> findAll(Pageable pageable);

}
