package com.iktpreobuka.eBook.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.iktpreobuka.eBook.entities.ParentEntity;
import com.iktpreobuka.eBook.entities.StudentEntity;

@Service
public class ParentDaoImpl implements ParentDao {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<ParentEntity> findParentsForClass(String classSign) {
		String sql = "select p from ParentEntity p left join p.students pst, ClassEntity c left join c.students cst " + "where pst.id = cst.id "
				+ "and c.classSign = :classSign";

		Query query = em.createQuery(sql);
		query.setParameter("classSign", classSign);

		List<ParentEntity> result = new ArrayList<>();
		result = query.getResultList();
		return result;
	}

}
