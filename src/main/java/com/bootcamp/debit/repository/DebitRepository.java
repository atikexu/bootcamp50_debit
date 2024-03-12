package com.bootcamp.debit.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bootcamp.debit.entity.Debit;

/**
 * Clase Repositorio para los métodos de acceso a la base de datos
 */
public interface DebitRepository extends ReactiveMongoRepository<Debit, String> {
	
}
