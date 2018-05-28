package com.iktpreobuka.eBook.entities.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iktpreobuka.eBook.deserializer.CustomDateDeserializer;
import com.iktpreobuka.eBook.enums.UserTitle;
import com.iktpreobuka.eBook.serializer.CustomDateSerializer;

public class UserRegisterDTO {

	private String firstName;
	private String lastName;
	private UserTitle title;
	private String email;
	private String jmbg;
	private Date dateOfBirth;
	private String address;

	public UserRegisterDTO(String firstName, String lastName, UserTitle title, String email, String jmbg,
			Date dateOfBirth, String address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.title = title;
		this.email = email;
		this.jmbg = jmbg;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
	}

	public UserRegisterDTO() {
		super();
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public UserTitle getTitle() {
		return title;
	}

	public String getEmail() {
		return email;
	}

	public String getJmbg() {
		return jmbg;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public String getAddress() {
		return address;
	}

}
