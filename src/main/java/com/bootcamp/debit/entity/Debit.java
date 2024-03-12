package com.bootcamp.debit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotEmpty;

/**
 * Clase de entidad
 */
@Data
@AllArgsConstructor
@Document(collection="debit")
public class Debit {
	@Id
	private String id;
	@NotEmpty
	private String customerId;
	@NotEmpty
	private String cardNumber;
	@NotEmpty
	private Double amount;
	@NotEmpty
	private String customerType;
	
	private LocalDateTime debitDate;
	
	private List<String> accountId;
	
	private String productType;
}
