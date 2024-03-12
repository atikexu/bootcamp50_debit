package com.bootcamp.debit.dto;

import com.bootcamp.debit.entity.Debit;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitResponse {

	private String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Debit debit;

}
