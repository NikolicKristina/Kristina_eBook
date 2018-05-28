package com.iktpreobuka.eBook.services;

import java.util.List;

import com.iktpreobuka.eBook.entities.SubjectEntity;

public interface SubjectDao {

	public List<SubjectEntity> findSubjectsForStudent(Integer studentId);

	public List<SubjectEntity> findSubjectsForTeacherAndClass(Integer teacherId, Integer classId);

	public List<SubjectEntity> findSubjectsForClass(Integer classId);

}
