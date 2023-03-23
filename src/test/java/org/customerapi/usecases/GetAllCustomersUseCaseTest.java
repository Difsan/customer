package org.customerapi.usecases;

import org.customerapi.domain.collection.Customer;
import org.customerapi.repository.ICustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GetAllCustomersUseCaseTest {

    @Mock
    ICustomerRepository repository;

    ModelMapper mapper;

    GetAllCustomersUseCase getAllCustomersUseCase;

    @BeforeEach
    void setUp() {
        mapper = new ModelMapper();
        getAllCustomersUseCase = new GetAllCustomersUseCase(repository, mapper);
    }

    @Test
    @DisplayName("getAllCustomers_Success")
    void getAllCustomers(){
        var fluxCustomers = Flux.just(new Customer("Pedro", "Marquez",
                "pm@gmail.com", "3049876875"), new Customer("Sandra", "Garcia",
                "sang@gmail.com", "3002874965"), new Customer("Juan", "Nieves",
                "jn23@gmail.com", "31289746987") );

        Mockito.when(repository.findAll()).thenReturn(fluxCustomers);

        var response = getAllCustomersUseCase.get();

        StepVerifier.create(response)
                .expectNextCount(3)
                .verifyComplete();

        Mockito.verify(repository, times(1)).findAll();

    }

    @Test
    @DisplayName("getAllCustomers_Failed")
    void getAllCustomers_Failed(){
        Mockito.when(repository.findAll()).thenReturn(Flux.error(
                new Throwable(HttpStatus.NOT_FOUND.toString())));

        var response = getAllCustomersUseCase.get();

        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable != null &&
                        throwable.getMessage().equals(HttpStatus.NOT_FOUND.toString()))
                .verify();

        Mockito.verify(repository, times(1)).findAll();
    }
}