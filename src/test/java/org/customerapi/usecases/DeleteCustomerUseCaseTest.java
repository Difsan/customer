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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DeleteCustomerUseCaseTest {

    @Mock
    ICustomerRepository repository;

    ModelMapper mapper;

    DeleteCustomerUseCase deleteCustomerUseCase;

    @BeforeEach
    void setUp(){
        mapper = new ModelMapper();
        deleteCustomerUseCase = new DeleteCustomerUseCase(repository, mapper);
    }

    @Test
    @DisplayName("deleteCustomerById_Success")
    void deleteCustomerById(){
        var customer = new Customer("Juan", "Nieves",
                "jn23@gmail.com", "31289746987");
        customer.setId("1");

        Mockito.when(repository.findById(Mockito.any(String.class))).thenReturn(Mono.just(customer));

        Mockito.when(repository.deleteById(Mockito.any(String.class))).thenReturn(Mono.empty());

        var result = deleteCustomerUseCase.apply("1");

        StepVerifier.create(result)
                .expectNext()
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).findById("1");
        Mockito.verify(repository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("deleteCustomerById_Failed")
    void deleteFlowerById_Failed(){
        Mockito.when(repository.findById(Mockito.any(String.class)))
                .thenReturn(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())));

        var result = deleteCustomerUseCase.apply("1");

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable != null &&
                        throwable.getMessage().equals(HttpStatus.NOT_FOUND.toString()))
                .verify();

        Mockito.verify(repository, times(1)).findById("1");
        Mockito.verify(repository, never()).deleteById("1");
    }
}