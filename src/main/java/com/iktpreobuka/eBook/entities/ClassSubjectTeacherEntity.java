package com.iktpreobuka.eBook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(ClassSubjectTeacherEntityPK.class)
public class ClassSubjectTeacherEntity {

	@Id
	@Column(insertable = false, updatable = false)
	private Integer classId;

	@Id
	@Column(insertable = false, updatable = false)
	private Integer subjectId;

	@Id
	@Column(insertable = false, updatable = false)
	private Integer teacherId;

	public ClassSubjectTeacherEntity() {
		super();
	}

	public ClassSubjectTeacherEntity(Integer classId, Integer subjectId, Integer teacherId) {
		super();
		this.classId = classId;
		this.subjectId = subjectId;
		this.teacherId = teacherId;
	}

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
}