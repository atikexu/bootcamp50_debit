package com.bootcamp.debit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.bootcamp.debit.dto.DebitResponse;
import com.bootcamp.debit.entity.Debit;
import com.bootcamp.debit.service.DebitService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebitControllerTest {

    private WebTestClient webTestClient;

    @InjectMocks
    private DebitController debitController;

    @Mock
    private DebitService debitService;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(debitController).build();
    }

    @Test
    @DisplayName("retorna exito si se recupera 2 elementos")
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
        
        when(debitService.getAll()).thenReturn(Flux.just(entidad1, entidad2));

        webTestClient.get()
                .uri("/debit")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Debit.class)
                .hasSize(2)
                .contains(entidad1, entidad2);
    }
    
    @Test
    @DisplayName("retorna exito si se recupera un elemento")
    void getDebitById() {
    	LocalDateTime time = LocalDateTime.now();
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit entidad1 = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");

        when(debitService.geDebitById(any())).thenReturn(Mono.just(entidad1));

        webTestClient.get()
                .uri("/debit/" + "00001")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Debit.class)
                .isEqualTo(entidad1);
    }
    
    @Test
    void saveCDebit() {
    	LocalDateTime time = LocalDateTime.now();
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit entidad1 = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");
        DebitResponse debitResponse = new DebitResponse("Debit card created", entidad1);

        when(debitService.saveDebit(any())).thenReturn(Mono.just(debitResponse));

        webTestClient.post()
                .uri("/debit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(entidad1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DebitResponse.class).isEqualTo(debitResponse);
    }
    
    @Test
    void updateDebit() {
    	LocalDateTime time = LocalDateTime.now();
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit entidad1 = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");

        when(debitService.updateDebit(any())).thenReturn(Mono.just(entidad1));

        webTestClient.put()
                .uri("/debit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(entidad1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Debit.class).isEqualTo(entidad1);
    }
    
    @Test
    void deleteCredit() {
    	LocalDateTime time = LocalDateTime.now();
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit entidad1 = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");

        when(debitService.deleteDebit(any())).thenReturn(Mono.just(entidad1));

        webTestClient.delete()
                .uri("/debit/" + "00001")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Debit.class).isEqualTo(entidad1);
    }
    
    @Test
    void getDebitByIdConsumer() {
    	LocalDateTime time = LocalDateTime.now();
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit entidad1 = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");

        when(debitService.getDebitByIdCustomer(any())).thenReturn(Mono.just(entidad1));

        webTestClient.get()
                .uri("/debit/customer/" + "1000001")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Debit.class)
                .isEqualTo(entidad1);
    }
    
    @Test
    void getAllDebitByIdConsumer() {
    	LocalDateTime time = LocalDateTime.now();
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit entidad1 = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");
        List<String> accounts2 = new ArrayList<>();
        accounts2.add("10101003");
        accounts2.add("10101004");
        Debit entidad2 = new Debit("00002", "1000001", "20230002", 200.0, "PERSONA", time, accounts2, "DEBIT_CARD");
        
        when(debitService.getAllDebitByIdConsumer(any())).thenReturn(Flux.just(entidad1, entidad2));

        webTestClient.get()
                .uri("/debit/customer/alldebits/" + "1000001")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Debit.class)
                .hasSize(2)
                .contains(entidad1, entidad2);
    }
    
    @Test
    void updateDebitDeposit() {
    	LocalDateTime time = LocalDateTime.now();
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit entidad1 = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");
        DebitResponse debitResponse = new DebitResponse("Successful transaction", entidad1);

        when(debitService.updateDebitDeposit(any())).thenReturn(Mono.just(debitResponse));

        webTestClient.post()
                .uri("/debit/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(entidad1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DebitResponse.class).isEqualTo(debitResponse);
    }
    
    @Test
    void updateDebitPay() {
    	LocalDateTime time = LocalDateTime.now();
        List<String> accounts1 = new ArrayList<>();
        accounts1.add("10101001");
        accounts1.add("10101002");
        Debit entidad1 = new Debit("00001", "1000001", "20230001", 100.0, "PERSONA", time, accounts1, "DEBIT_CARD");
        DebitResponse debitResponse = new DebitResponse("Successful transaction", entidad1);

        when(debitService.updateDebitPay(any())).thenReturn(Mono.just(debitResponse));

        webTestClient.post()
                .uri("/debit/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(entidad1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DebitResponse.class).isEqualTo(debitResponse);
    }

//    @Test
//    void create() {
//
//        Product entidad = new Product();
//        entidad.setProductId(1L);
//        entidad.setCode("123123123");
//        entidad.setEan("7777777");
//        entidad.setName("Nuevo producto");
//        entidad.setProductLocation("1A");
//        entidad.setCanBeSold(1);
//        entidad.setIsActive(1);
//        entidad.setSalesPrice(new BigDecimal(10));
//        entidad.setSalesPriceWithTax(new BigDecimal(12));
//        when(productService.create(entidad)).thenReturn(Mono.just(entidad));
//
//        webTestClient.post()
//                .uri("/products")
//                .exchange()
//  .expectStatus().isOk()
//  .expectBody(Product.class)
//  .isEqualTo(entidad);
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void updateEan() {
//    }
//
//    @Test
//    void read() {
//        final long productId = 1;
//        Product entidad = new Product();
//        entidad.setProductId(1L);
//        entidad.setCode("123123");
//        when(productService.read(productId)).thenReturn(Mono.just(entidad));
//
//        webTestClient.get()
//                .uri("/products/1")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(Product.class)
//                .isEqualTo(entidad);
//    }
//
//    @Test
//    void delete() {
//    }
}