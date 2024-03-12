package com.bootcamp.debit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.debit.dto.DebitResponse;
import com.bootcamp.debit.entity.Debit;
import com.bootcamp.debit.service.DebitService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/debit")
@RequiredArgsConstructor
public class DebitController {
	
	@Autowired
    private DebitService debitService;

    /**
     *
     * Obtener la información de todas las tarjetas debito
     * @return
     */
    @GetMapping
    public Flux<Debit> getAll(){
        return debitService.getAll();
    }

    /**
     *
     * Obtene tarjetas debito por Id
     * @param id
     * @return
     */
    @GetMapping ("/{id}")
    public Mono<Debit> getDebitById(@PathVariable String id){
        return debitService.geDebitById(id);
    }

    /**
     * Registrar debit card
     * @param debit
     * @return
     */
    @PostMapping
    public Mono<DebitResponse> saveCDebit(@RequestBody Debit debit){
        return debitService.saveDebit(debit);
    }

    @PutMapping
    public Mono<Debit> updateDebit(@RequestBody Debit debit){
        return debitService.updateDebit(debit);
    }

    @DeleteMapping("/{id}")
    public Mono<Debit> deleteCredit(@PathVariable String id){
        return debitService.deleteDebit(id);
    }
    
    @GetMapping ("/customer/{id}")
    public Mono<Debit> getDebitByIdConsumer(@PathVariable String id){
        return debitService.getDebitByIdCustomer(id);
    }
    
    @GetMapping ("/customer/alldebits/{id}")
    public Flux<Debit> getAllDebitByIdConsumer(@PathVariable String id){
        return debitService.getAllDebitByIdConsumer(id);
    }
    
    @PostMapping("/deposit")
    public Mono<DebitResponse> updateDebitDeposit(@RequestBody Debit debit){
        return debitService.updateDebitDeposit(debit);
    }
    
    @PostMapping("/pay")
    public Mono<DebitResponse> updateDebitPay(@RequestBody Debit debit){
        return debitService.updateDebitPay(debit);
    }

}
