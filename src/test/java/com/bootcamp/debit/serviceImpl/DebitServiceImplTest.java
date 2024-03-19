package com.bootcamp.debit.serviceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bootcamp.debit.clients.AccountsRestClient;
import com.bootcamp.debit.clients.TransactionsRestClient;
import com.bootcamp.debit.dto.Account;
import com.bootcamp.debit.dto.DebitResponse;
import com.bootcamp.debit.dto.Transaction;
import com.bootcamp.debit.entity.Debit;
import com.bootcamp.debit.repository.DebitRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class DebitServiceImplTest {

    @InjectMocks
    private DebitServiceImpl debitServiceImpl;

    @Mock
    private DebitRepository debitRepository;
    
    @Mock
    private AccountsRestClient accountsRestClient;
    
    @Mock
    private TransactionsRestClient transactionsRestClient;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAll() {
        LocalDateTime time = LocalDateTime.now();
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit entidad1 = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");
        List<String> accounts2 = new ArrayList<>();
        accounts2.add("10101001");
        accounts2.add("10101002");
        Debit entidad2 = new Debit("00002", "1000002", "20230002", 200.0, "PERSONA", time, accounts2, "DEBIT_CARD");

        when(debitRepository.findAll()).thenReturn(Flux.just(entidad1, entidad2));

        Flux<Debit> resultado = debitServiceImpl.getAll();

        StepVerifier.create(resultado)
                .expectNext(entidad1)
                .expectNext(entidad2)
                .expectComplete()
                .verify();
        verify(debitRepository, times(1)).findAll();
    }
    
    @Test
    void geDebitById() {
        LocalDateTime time = LocalDateTime.now();
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit entidad1 = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");

        when(debitRepository.findById("00001")).thenReturn(Mono.just(entidad1));

        Mono<Debit> resultado = debitServiceImpl.geDebitById("00001");

        StepVerifier.create(resultado)
                .expectNext(entidad1)
                .expectComplete()
                .verify();
    }

    @Test
    void saveDebit() {
    	LocalDateTime time = LocalDateTime.now();
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit entidad1 = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");
        Account account = new Account("10101001", "1000001", 2, "PLAZO_FIJO", 100.0, 0.0, 20, 15, time, "000000000001", "PERSON", 100.0);
        DebitResponse debitResponse = new DebitResponse("Debit card created", entidad1);
        
        when(debitRepository.save(entidad1)).thenReturn(Mono.just(entidad1));
        when(accountsRestClient.getAccountById("10101001")).thenReturn(Mono.just(account));

        Mono<DebitResponse> resultado = debitServiceImpl.saveDebit(entidad1);

        StepVerifier.create(resultado)
                .expectNext(debitResponse)
                .expectComplete()
                .verify();
    }
    
    @Test
    void updateDebit() {
    	LocalDateTime time = LocalDateTime.now();
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit entidad1 = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");
        
        when(debitRepository.findById("00001")).thenReturn(Mono.just(entidad1));
        when(debitRepository.save(entidad1)).thenReturn(Mono.just(entidad1));

        Mono<Debit> resultado = debitServiceImpl.updateDebit(entidad1);

        StepVerifier.create(resultado)
                .expectNext(entidad1)
                .expectComplete()
                .verify();
    }
    
    @Test
    void deleteDebit() {
    	LocalDateTime time = LocalDateTime.now();
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit entidad1 = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");
        
        when(debitRepository.findById("00001")).thenReturn(Mono.just(entidad1));
        when(debitRepository.delete(entidad1)).thenReturn(Mono.empty());

        Mono<Debit> resultado = debitServiceImpl.deleteDebit("00001");

        StepVerifier.create(resultado)
        		.expectNext(entidad1) 
                .expectComplete()
                .verify();
    }
    
    @Test
    void getDebitByIdCustomer() {
        LocalDateTime time = LocalDateTime.now();
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit entidad1 = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");
        List<String> accounts2 = new ArrayList<>();
        accounts2.add("10101001");
        accounts2.add("10101002");
        Debit entidad2 = new Debit("00002", "1000002", "20230002", 200.0, "PERSONA", time, accounts2, "DEBIT_CARD");

        when(debitRepository.findAll()).thenReturn(Flux.just(entidad1, entidad2));

        Mono<Debit> resultado = debitServiceImpl.getDebitByIdCustomer("1000001");

        StepVerifier.create(resultado)
                .expectNext(entidad1)
                .expectComplete()
                .verify();
        verify(debitRepository, times(1)).findAll();
    }
    
    @Test
    void getAllDebitByIdConsumer() {
        LocalDateTime time = LocalDateTime.now();
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit entidad1 = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");
        List<String> accounts2 = new ArrayList<>();
        accounts2.add("10101001");
        accounts2.add("10101002");
        Debit entidad2 = new Debit("00002", "1000001", "20230002", 200.0, "PERSONA", time, accounts2, "DEBIT_CARD");

        when(debitRepository.findAll()).thenReturn(Flux.just(entidad1, entidad2));

        Flux<Debit> resultado = debitServiceImpl.getAllDebitByIdConsumer("1000001");

        StepVerifier.create(resultado)
                .expectNext(entidad1)
                .expectNext(entidad2)
                .expectComplete()
                .verify();
        verify(debitRepository, times(1)).findAll();
    }
    
    @Test
    void updateDebitDeposit() {
    	LocalDateTime time = LocalDateTime.now();
    	Debit newDebit1 = new Debit();
    	newDebit1.setId("00001");
    	newDebit1.setAmount(100.0);
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit debit = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");
        Account account = new Account("10101001", "1000001", 2, "PLAZO_FIJO", 100.0, 0.0, 20, 15, time, "000000000001", "PERSON", 100.0);
        Debit newDebit = new Debit("00001", "1000001", "20230001", 200.0, "PERSONA", time, accounts1, "DEBIT_CARD");
        Account newAccount = new Account("10101001", "1000001", 2, "PLAZO_FIJO", 200.0, 0.0, 20, 15, time, "000000000001", "PERSON", 100.0);
        DebitResponse debitResponse = new DebitResponse("Successful transaction", newDebit);
        Transaction transaction = new Transaction("00009", "DEBIT_CARD", "00001", "1000001", "DEPOSITO", 100.0, time, "PERSONA", 200.0);
        
        when(debitRepository.findById("00001")).thenReturn(Mono.just(debit));
        when(debitRepository.save(debit)).thenReturn(Mono.just(newDebit));
        when(accountsRestClient.getAccountById("10101001")).thenReturn(Mono.just(account));
        when(accountsRestClient.updateAccount(account)).thenReturn(Mono.just(newAccount));
        when(transactionsRestClient.createTransaction(any(Transaction.class))).thenReturn(Mono.just(transaction));

        Mono<DebitResponse> resultado = debitServiceImpl.updateDebitDeposit(newDebit1);

        StepVerifier.create(resultado)
                .expectNext(debitResponse)
                .expectComplete()
                .verify();
    }
    
    @Test
    void updateDebitPay() {
    	LocalDateTime time = LocalDateTime.now();
    	Debit newDebit1 = new Debit();
    	newDebit1.setId("00001");
    	newDebit1.setAmount(30.0);
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit debit = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");
        Account account = new Account("10101001", "1000001", 2, "PLAZO_FIJO", 100.0, 0.0, 20, 15, time, "000000000001", "PERSON", 100.0);
        Debit newDebit = new Debit("00001", "1000001", "20230001", 70.0, "PERSONA", time, accounts1, "DEBIT_CARD");
        Account newAccount = new Account("10101001", "1000001", 2, "PLAZO_FIJO", 70.0, 0.0, 20, 15, time, "000000000001", "PERSON", 100.0);
        DebitResponse debitResponse = new DebitResponse("Successful transaction", newDebit);
        Transaction transaction = new Transaction("00009", "DEBIT_CARD", "00001", "1000001", "DEPOSITO", 30.0, time, "PERSONA", 70.0);
        
        when(debitRepository.findById("00001")).thenReturn(Mono.just(debit));
        when(accountsRestClient.getAllAccountXCustomerId(any())).thenReturn(Flux.just(account));
        when(accountsRestClient.updateAccount(account)).thenReturn(Mono.just(newAccount));
        when(debitRepository.save(debit)).thenReturn(Mono.just(newDebit));
        when(transactionsRestClient.createTransaction(any(Transaction.class))).thenReturn(Mono.just(transaction));

        Mono<DebitResponse> resultado = debitServiceImpl.updateDebitPay(newDebit1);

        StepVerifier.create(resultado)
                .expectNext(debitResponse)
                .expectComplete()
                .verify();
    }
//
//    @Test
//    void read() {
//        final long productId = 1L;
//        Product entidad = new Product();
//        entidad.setProductId(productId);
//        when(productPersistence.read(productId)).thenReturn(Mono.just(entidad));
//
//        Mono<Product> resultado = productService.read(productId);
//
//        StepVerifier.create(resultado)
//                .expectNext(entidad)
//                .expectComplete()
//                .verify();
//    }
//
//    @Test
//    void delete() {
//        final long productId = 1L;
//        when(productPersistence.delete(productId)).thenReturn(Mono.empty());
//
//        Mono<ProductIdAndNameDto> resultado = productService.delete(productId);
//
//        StepVerifier.create(resultado)
//                .expectComplete()
//                .verify();
//    }
//
////    @Test
////    void update() {
////        Product entidad = new Product();
////        entidad.setProductId(1L);
////        entidad.setCode("123123");
////        entidad.setName("Nuevo producto");
////        when(productPersistence.update(entidad)).thenReturn(Mono.just(entidad));
////
////        Mono<Product> resultado = productService.update(entidad);
////
////        StepVerifier.create(resultado)
////                .expectNext(entidad)
////                .expectComplete()
////                .verify();
////    }

}