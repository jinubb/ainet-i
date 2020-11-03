package com.example.model;

import com.example.entity.Vehicle;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ControlResponse {
	private boolean isControl;
	private Vehicle vehicle;
}
