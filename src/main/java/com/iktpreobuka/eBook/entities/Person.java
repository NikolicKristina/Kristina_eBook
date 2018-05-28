package com.iktpreobuka.eBook.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iktpreobuka.eBook.deserializer.CustomDateDeserializer;
import com.iktpreobuka.eBook.enums.UserTitle;
import com.iktpreobuka.eBook.security.Views;
import com.iktpreobuka.eBook.serializer.CustomDateSerializer;

@MappedSuperclass
public abstract class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Public.class)
	protected Integer id;
	@Column
	@JsonView(Views.Admin.class)
	protected UserTitle title;
	@Column
	@JsonView(Views.Public.class)
	protected String firstName;
	@Column
	@JsonView(Views.Public.class)
	protected String lastName;
	@Column
	@JsonView(Views.Admin.class)
	protected String email;
	@Column
	@JsonView(Views.Admin.class)
	protected Date dateOfBirth;
	@Column
	@JsonView(Views.Admin.class)
	protected String address;
	@Column(name = "JMBG", unique = true, length = 13)
	@JsonView(Views.Admin.class)
	protected String jmbg;

	@Version
	private Integer version;

	public Person() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	public UserTitle getTitle() {
		return title;
	}

	public void setTitle(UserTitle title) {
		this.title = title;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
