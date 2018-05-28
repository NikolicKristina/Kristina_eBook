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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.eBook.security.Views;

@Entity
@Where(clause = "deleted!=true")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ClassEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Public.class)
	private Integer id;
	
	@Column(unique = true, length = 2)
	@JsonView(Views.Public.class)
	private String classSign;

	@OneToMany(mappedBy = "schoolClass", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JsonView(Views.Private.class)
	private List<StudentEntity> students = new ArrayList<>();

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonView(Views.Public.class)
	@JoinColumn(name = "year")
	
	private YearEntity year;

	@Version
	private Integer version;
	@Column
	private boolean deleted;

	public ClassEntity() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClassSign() {
		return classSign;
	}

	public void setClassSign(String classSign) {
		this.classSign = classSign;
	}

	@JsonManagedReference(value = "StudentReference")
	public List<StudentEntity> getStudents() {
		return students;
	}

	public void setStudents(List<StudentEntity> students) {
		this.students = students;
	}

	@JsonBackReference(value = "YearClassReference")
	public YearEntity getYear() {
		return year;
	}

	public void setYear(YearEntity year) {
		this.year = year;
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

}
