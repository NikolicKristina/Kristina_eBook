package com.iktpreobuka.eBook.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.iktpreobuka.eBook.entities.ClassEntity;
import com.iktpreobuka.eBook.entities.StudentEntity;

@Service
public class ClassDaoImpl implements ClassDao {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<ClassEntity> findClassesForTeacher(Integer teacherId) {
		String sql = "select distinct(cl) from ClassEntity cl, ClassSubjectTeacherEntity cst "
				+ "where cl.id = cst.classId and cst.teacherId = :teacherId";

		Query query = em.createQuery(sql);
		query.setParameter("teacherId", teacherId);

		List<ClassEntity> result = query.getResultList();
		return result;
	}

	@Override
	public List<ClassEntity> findClassesBySubjectAndTeacherAndStudent(Integer subjectId, Integer teacherId,
			Integer studentId) {
		String sql = "select distinct(c) from ClassEntity c, StudentEntity s, ClassSubjectTeacherEntity cst "
				+ "where s.id = :studentId and s.schoolClass.id = c.id and cst.classId = s.schoolClass.id and cst.subjectId = :subjectId and cst.teacherId = :teacherId";

		Query query = em.createQuery(sql);
		query.setParameter("subjectId", subjectId);
		query.setParameter("teacherId", teacherId);
		query.setParameter("studentId", studentId);

		List<ClassEntity> result = new ArrayList<>();
		result = query.getResultList();
		return result;
	}

}
