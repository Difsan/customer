package org.customerapi.usecases;


import org.customerapi.domain.collection.Customer;
import org.customerapi.domain.dto.CustomerDTO;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GetCustomerByIdUseCaseTest {

    @Mock
    ICustomerRepository repository;

    ModelMapper mapper;

    GetCustomerByIdUseCase getCustomerByIdUseCase;

    @BeforeEach
    void setUp(){
        mapper = new ModelMapper();
        getCustomerByIdUseCase = new GetCustomerByIdUseCase(repository, mapper);
    }

    @Test
    @DisplayName("getCustomerById_Success")
    void getCustomerById(){
        var customer = new Customer("Pedro", "Marquez",
                "pm@gmail.com", "3049876875");
        customer.setId("1");

        Mockito.when(repository.findById(Mockito.any(String.class))).thenReturn(Mono.just(customer));

        var result = getCustomerByIdUseCase.apply("1");

        StepVerifier.create(result)
                .expectNext(mapper.map(customer, CustomerDTO.class))
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).findById("1");
    }

    @Test
    @DisplayName("getCustomerById_Failed")
    void getCustomerById_Failed(){
        Mockito.when(repository.findById(Mockito.any(String.class)))
                .thenReturn(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())));

        var result = getCustomerByIdUseCase.apply("1");

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable != null &&
                        throwable.getMessage().equals(HttpStatus.NOT_FOUND.toString()))
                .verify();

        Mockito.verify(repository, times(1)).findById("1");
    }
}