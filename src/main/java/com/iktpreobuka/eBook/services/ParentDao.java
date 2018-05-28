package com.iktpreobuka.eBook.services;

import java.util.List;

import com.iktpreobuka.eBook.entities.ParentEntity;

public interface ParentDao {

	public List<ParentEntity> findParentsForClass(String ClassSign);

}
