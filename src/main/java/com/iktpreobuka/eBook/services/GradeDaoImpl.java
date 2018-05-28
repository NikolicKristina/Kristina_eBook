package com.iktpreobuka.eBook.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.iktpreobuka.eBook.entities.GradeEntity;

@Service
public class GradeDaoImpl implements GradeDao {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<GradeEntity> findGradesForStudent(Integer studentId, Integer subjectId) {
		String sql = "select g from GradeEntity g "
					+ "where g.subject.id = :subjectId and g.student.id = :studentId and g.deleted=false ";

		Query query = em.createQuery(sql);
		query.setParameter("subjectId", subjectId);
		query.setParameter("studentId", studentId);

		List<GradeEntity> result = query.getResultList();
		return result;
	}

	@Override
	public List<GradeEntity> findGradesForSemester1(Integer studentId, Integer subjectId) {
		String sql = "select g from GradeEntity g "
					+ "where g.subject.id = :subjectId and g.student.id = :studentId and g.deleted = false and g.semester = 1 and g.type != 3";

		Query query = em.createQuery(sql);
		query.setParameter("subjectId", subjectId);
		query.setParameter("studentId", studentId);

		List<GradeEntity> result = query.getResultList();
		return result;
	}

	@Override
	public List<GradeEntity> findGradesForSemester2(Integer studentId, Integer subjectId) {
		String sql = "select g from GradeEntity g "
					+ "where g.subject.id = :subjectId and g.student.id = :studentId and g.deleted=false and g.semester=2";

		Query query = em.createQuery(sql);
		query.setParameter("subjectId", subjectId);
		query.setParameter("studentId", studentId);

		List<GradeEntity> result = query.getResultList();
		return result;
	}

	@Override
	public List<GradeEntity> findFinalGradeForSemester1(Integer studentId, Integer subjectId) {
		String sql = "select g from GradeEntity g "
					+ "where g.subject.id = :subjectId and g.student.id = :studentId and g.deleted=false and g.semester=1 and g.type=3";

		Query query = em.createQuery(sql);
		query.setParameter("subjectId", subjectId);
		query.setParameter("studentId", studentId);

		List<GradeEntity> result = query.getResultList();
		return result;
	}
}
