package com.bootcamp.debit.service;

import com.bootcamp.debit.dto.DebitResponse;
import com.bootcamp.debit.entity.Debit;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitService {

    Flux<Debit> getAll();
    Mono<Debit> geDebitById(String debitId);
    Mono<DebitResponse> saveDebit(Debit debit);
    Mono<Debit> updateDebit(Debit debit);
    Mono<Debit> deleteDebit(String debitId);
	Mono<Debit> getDebitByIdCustomer(String customerId);
	Flux<Debit> getAllDebitByIdConsumer(String customerId);
	Mono<DebitResponse> updateDebitDeposit(Debit debit);
	
	Mono<DebitResponse> updateDebitPay(Debit debit);
}
