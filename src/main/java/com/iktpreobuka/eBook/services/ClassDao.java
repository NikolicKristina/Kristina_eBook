package com.iktpreobuka.eBook.services;

import java.util.List;

import com.iktpreobuka.eBook.entities.ClassEntity;

public interface ClassDao {

	public List<ClassEntity> findClassesForTeacher(Integer teacherId);

	public List<ClassEntity> findClassesBySubjectAndTeacherAndStudent(Integer subjectId, Integer teacherId,
			Integer studentId);

}
