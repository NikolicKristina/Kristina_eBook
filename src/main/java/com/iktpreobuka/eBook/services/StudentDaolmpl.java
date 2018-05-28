package com.iktpreobuka.eBook.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.iktpreobuka.eBook.entities.StudentEntity;

@Service
public class StudentDaolmpl implements StudentDao {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<StudentEntity> findStudentsByParentId(Integer parentId) {
		String sql = "select s from StudentEntity s left join fetch s.parents p where p.id = :parentId ";

		Query query = em.createQuery(sql);
		query.setParameter("parentId", parentId);

		List<StudentEntity> result = new ArrayList<>();
		result = query.getResultList();
		return result;
	}

	@Override
	public List<StudentEntity> findStudentsByClass(String classSign) {
		String sql = "select s from StudentEntity s, ClassEntity c "
				 + "where s.schoolClass.id = c.id and c.classSign = :classSign";

		Query query = em.createQuery(sql);
		query.setParameter("classSign", classSign);

		List<StudentEntity> result = new ArrayList<>();
		result = query.getResultList();
		return result;
	}
	
	@Override
	public List<StudentEntity> findStudentsBySubject(Integer subjectId) {
		String sql = "select distinct(s) from StudentEntity s, ClassEntity c, YearEntity y, SubjectEntity sub "
				 + "where s.schoolClass.id = c.id and c.year.id = sub.year.id and sub.id = :subjectId";

		Query query = em.createQuery(sql);
		query.setParameter("subjectId", subjectId);

		List<StudentEntity> result = new ArrayList<>();
		result = query.getResultList();
		return result;
	}

	@Override
	public List<StudentEntity> findStudentsBySubjectAndTeacher(Integer subjectId, Integer teacherId, Integer classId) {
		String sql = "select distinct(s) from StudentEntity s, ClassSubjectTeacherEntity cst "
				+ "where s.schoolClass.id = cst.classId and cst.classId = :classId and cst.subjectId = :subjectId and cst.teacherId = :teacherId";

		Query query = em.createQuery(sql);
		query.setParameter("subjectId", subjectId);
		query.setParameter("teacherId", teacherId);
		query.setParameter("classId", classId);

		List<StudentEntity> result = new ArrayList<>();
		result = query.getResultList();
		return result;
	}

}
