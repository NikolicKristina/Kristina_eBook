package com.iktpreobuka.eBook.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchoolRegisterDTO {

	private String classSign;
	private Integer year;
	private String name;
	private Integer weeklyHours;

	public SchoolRegisterDTO() {
		super();
	}

	public SchoolRegisterDTO(String classSign, Integer year, String name, Integer weeklyHours) {
		super();
		this.classSign = classSign;
		this.year = year;
		this.name = name;
		this.weeklyHours = weeklyHours;
	}

	//@JsonProperty("classSign")
	public String getClassSign() {
		return classSign;
	}

	//@JsonProperty("year")
	public Integer getYear() {
		return year;
	}

	//@JsonProperty("subjectName")
	public String getName() {
		return name;
	}

	//@JsonProperty("subjectWeeklyHours")
	public Integer getWeeklyHours() {
		return weeklyHours;
	}
}
