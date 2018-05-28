package com.iktpreobuka.eBook.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.eBook.security.Views;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Where(clause = "deleted != true")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Admin.class)
	private Integer id;
	@Column
	private String password;
	@Column
	@JsonView(Views.Admin.class)
	private String email;
	@Column
	@JsonView(Views.Admin.class)
	private boolean deleted;

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private AdminEntity admin;

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private ParentEntity parent;

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private StudentEntity student;

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private TeacherEntity teacher;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "role")
	@JsonView(Views.Admin.class)
	private RoleEntity role;

	@Version
	private Integer version;

	public UserEntity() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonBackReference(value = "UserAdminReference")
	public AdminEntity getAdmin() {
		return admin;
	}

	public void setAdmin(AdminEntity admin) {
		this.admin = admin;
	}

	@JsonBackReference(value = "UserParentReference")
	public ParentEntity getParent() {
		return parent;
	}

	public void setParent(ParentEntity parent) {
		this.parent = parent;
	}

	@JsonBackReference(value = "UserStudentReference")
	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	@JsonBackReference(value = "UserTeacherReference")
	public TeacherEntity getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherEntity teacher) {
		this.teacher = teacher;
	}

	@JsonManagedReference(value = "UserRoleReference")
	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
