package com.iktpreobuka.eBook.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.eBook.security.Views;

@Entity
@Where(clause = "deleted != true")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ParentEntity extends Person {

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JoinTable(name = "Parent_Student", joinColumns = { @JoinColumn(name = "parent_id") }, inverseJoinColumns = {
			@JoinColumn(name = "student_id") })
	@JsonView(Views.Admin.class)
	private List<StudentEntity> students = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "user")
	@JsonView(Views.Admin.class)
	private UserEntity user;

	@Column
	@JsonView(Views.Admin.class)
	private boolean deleted;

	public ParentEntity() {
		super();
	}

	@JsonManagedReference(value = "ParentReference")
	public List<StudentEntity> getStudents() {
		return students;
	}

	public void setStudents(List<StudentEntity> students) {
		this.students = students;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@JsonManagedReference(value = "UserParentReference")
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}
