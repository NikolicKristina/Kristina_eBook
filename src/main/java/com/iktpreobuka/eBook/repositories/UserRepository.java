package com.iktpreobuka.eBook.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.eBook.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

	public UserEntity findUserByEmail(String email);

	public Page<UserEntity> findAll(Pageable pageable);

	public UserEntity findUserByPassword(String password);

}
