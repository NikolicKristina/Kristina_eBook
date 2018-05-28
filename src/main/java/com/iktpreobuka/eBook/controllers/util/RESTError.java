package com.iktpreobuka.eBook.controllers.util;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.eBook.security.Views;

public class RESTError {
	
	@JsonView(Views.Public.class)
	private Integer code;
	@JsonView(Views.Public.class)
	private String message;

	public RESTError() {
	}

	public RESTError(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
