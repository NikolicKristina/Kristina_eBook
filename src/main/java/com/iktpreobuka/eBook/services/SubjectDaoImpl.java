package com.iktpreobuka.eBook.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.iktpreobuka.eBook.entities.SubjectEntity;

@Service
public class SubjectDaoImpl implements SubjectDao {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<SubjectEntity> findSubjectsForStudent(Integer studentId) {
		String sql = "select distinct(s) from SubjectEntity s, ClassSubjectTeacherEntity cst, StudentEntity st "
				+ "where st.schoolClass.id = cst.classId and cst.subjectId = s.id and st.id = :studentId ";
		Query query = em.createQuery(sql);
		query.setParameter("studentId", studentId);

		List<SubjectEntity> result = new ArrayList<>();
		result = query.getResultList();
		return result;
	}

	@Override
	public List<SubjectEntity> findSubjectsForTeacherAndClass(Integer teacherId, Integer classId) {
		String sql = "select distinct(s) from SubjectEntity s, ClassSubjectTeacherEntity cst "
				+ "where cst.teacherId = :teacherId and cst.classId = :classId and cst.subjectId = s.id ";
		Query query = em.createQuery(sql);
		query.setParameter("teacherId", teacherId);
		query.setParameter("classId", classId);

		List<SubjectEntity> result = new ArrayList<>();
		result = query.getResultList();
		return result;
	}

	@Override
	public List<SubjectEntity> findSubjectsForClass(Integer classId) {
		String sql = "select distinct(s) from SubjectEntity s, ClassEntity c "
				+ "where s.year = c.year and c.id = :classId";
		Query query = em.createQuery(sql);
		query.setParameter("classId", classId);

		List<SubjectEntity> result = new ArrayList<>();
		result = query.getResultList();
		return result;
	}
}
