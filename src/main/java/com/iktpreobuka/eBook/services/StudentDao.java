package com.iktpreobuka.eBook.services;

import java.util.List;

import com.iktpreobuka.eBook.entities.StudentEntity;

public interface StudentDao {

	public List<StudentEntity> findStudentsByParentId(Integer id);

	public List<StudentEntity> findStudentsByClass(String classSign);

	public List<StudentEntity> findStudentsBySubjectAndTeacher(Integer subjectId, Integer teacherId, Integer classId);

	public List<StudentEntity> findStudentsBySubject(Integer subjectId);

}
