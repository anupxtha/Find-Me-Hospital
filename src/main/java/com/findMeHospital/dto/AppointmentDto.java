package com.findMeHospital.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppointmentDto {
	private int month;
	
	private long total;
	
	public AppointmentDto(int month, long total) {
		super();
		this.month = month;
		this.total = total;
	}
	
}
