package com.iktpreobuka.eBook.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.eBook.security.Views;

@Where(clause = "deleted != true")
@Entity
public class AdminEntity extends Person {

	@Column
	@JsonView(Views.Admin.class)
	private boolean deleted;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "user")
	@JsonView(Views.Admin.class)
	private UserEntity user;

	public AdminEntity() {
		super();
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@JsonManagedReference(value = "UserAdminReference")
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}
