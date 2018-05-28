package com.iktpreobuka.eBook.services;

import java.util.List;

import com.iktpreobuka.eBook.entities.GradeEntity;

public interface GradeDao {

	public List<GradeEntity> findGradesForStudent(Integer studentId, Integer subjectId);

	public List<GradeEntity> findGradesForSemester1(Integer studentId, Integer subjectId);

	public List<GradeEntity> findGradesForSemester2(Integer studentId, Integer subjectId);

	public List<GradeEntity> findFinalGradeForSemester1(Integer studentId, Integer subjectId);

}
