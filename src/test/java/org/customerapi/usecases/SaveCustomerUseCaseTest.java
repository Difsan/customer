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
class SaveCustomerUseCaseTest {

    @Mock
    ICustomerRepository repository;

    ModelMapper mapper;

    SaveCustomerUseCase saveCustomerUseCase;

    @BeforeEach
    void setUp() {
        mapper = new ModelMapper();
        saveCustomerUseCase = new SaveCustomerUseCase(repository, mapper);
    }

    @Test
    @DisplayName("saveCustomer_Success")
    void saveCustomer(){
        var customer =  new Customer("Sandra", "Garcia",
                "sang@gmail.com", "3002874965");

        var customerMono = Mono.just(customer);

        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customerMono);

        var result = saveCustomerUseCase.save(mapper.map(customer, CustomerDTO.class));

        StepVerifier.create(result)
                .expectNext(mapper.map(customer, CustomerDTO.class))
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).save(customer);
    }

    @Test
    @DisplayName("saveFlower_Failed")
    void saveFlower_Failed(){
        var customer =  new Customer("Sandra", "Garcia",
                "sang@gmail.com", "3002874965");

        Mockito.when(repository.save(Mockito.any(Customer.class)))
                .thenReturn(Mono.error(new Throwable(HttpStatus.BAD_REQUEST.toString())));

        var result = saveCustomerUseCase.save(mapper.map(customer, CustomerDTO.class));

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable != null &&
                        throwable.getMessage().equals(HttpStatus.BAD_REQUEST.toString()))
                .verify();

        Mockito.verify(repository, times(1)).save(customer);
    }
}