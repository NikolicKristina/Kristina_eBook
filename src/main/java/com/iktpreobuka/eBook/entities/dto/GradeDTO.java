package com.iktpreobuka.eBook.entities.dto;

import com.iktpreobuka.eBook.enums.GradeType;

public class GradeDTO {

	private Integer value;
	private GradeType type;
	private Integer semester;

	public GradeDTO() {
		super();
	}

	public GradeDTO(Integer value, GradeType type, Integer semester) {
		super();
		this.value = value;
		this.type = type;
		this.semester = semester;
	}

	public Integer getValue() {
		return value;
	}

	public GradeType getType() {
		return type;
	}

	public Integer getSemester() {
		return semester;
	}
}
