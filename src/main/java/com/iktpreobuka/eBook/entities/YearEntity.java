package com.iktpreobuka.eBook.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.eBook.security.Views;

@Entity
@Where(clause = "deleted != true")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class YearEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Public.class)
	private Integer id;
	@Column(unique = true)
	@JsonView(Views.Public.class)
	private Integer year;

	@OneToMany(mappedBy = "year", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JsonView(Views.Private.class)
	private List<ClassEntity> schoolClasses = new ArrayList<>();

	@OneToMany(mappedBy = "year", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JsonView(Views.Private.class)
	private List<SubjectEntity> subjects = new ArrayList<>();

	@Version
	private Integer version;
	@Column
	@JsonView(Views.Admin.class)
	private boolean deleted;

	public YearEntity() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@JsonManagedReference(value = "YearClassReference")
	public List<ClassEntity> getSchoolClasses() {
		return schoolClasses;
	}

	public void setSchoolClasses(List<ClassEntity> schoolClasses) {
		this.schoolClasses = schoolClasses;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@JsonManagedReference(value = "YearSubjectReference")
	public List<SubjectEntity> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<SubjectEntity> subjects) {
		this.subjects = subjects;
	}

}
