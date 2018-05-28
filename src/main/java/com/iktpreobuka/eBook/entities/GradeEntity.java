package com.iktpreobuka.eBook.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.eBook.enums.GradeType;
import com.iktpreobuka.eBook.security.Views;

@Entity
@Where(clause = "deleted != true")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class GradeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Public.class)
	private Integer id;
	@Column
	@JsonView(Views.Public.class)
	private Integer value;
	@Column
	@JsonView(Views.Public.class)
	private GradeType type;
	@Column
	@JsonView(Views.Public.class)
	private Date assignDate;
	@Column
	@JsonView(Views.Public.class)
	private Integer semester;
	@Column
	@JsonView(Views.Admin.class)
	private boolean deleted;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "teacher")
	@JsonView(Views.Admin.class)
	private TeacherEntity teacher;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "subject")
	@JsonView(Views.Admin.class)
	private SubjectEntity subject;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "student")
	@JsonView(Views.Admin.class)
	private StudentEntity student;

	@Version
	private Integer version;

	public GradeEntity() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public GradeType getType() {
		return type;
	}

	public void setType(GradeType type) {
		this.type = type;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	public Date getAssignDate() {
		return assignDate;
	}

	public void setAssignDate(Date assignDate) {
		this.assignDate = assignDate;
	}

	public Integer getSemester() {
		return semester;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@JsonManagedReference(value = "GradeReference")
	public TeacherEntity getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherEntity teacher) {
		this.teacher = teacher;
	}

	@JsonManagedReference(value = "GradeSubjectReference")
	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

	@JsonManagedReference(value = "GradeStudentReference")
	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
