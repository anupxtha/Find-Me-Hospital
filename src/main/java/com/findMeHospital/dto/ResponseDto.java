package com.findMeHospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
	private String message;
	
	private String type;
	
	private Object list;
	
	public ResponseDto(String message, String type) {
		super();
		this.message = message;
		this.type = type;
	}

}
