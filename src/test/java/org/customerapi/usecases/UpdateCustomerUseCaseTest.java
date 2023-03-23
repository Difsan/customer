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

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerUseCaseTest {

    @Mock
    ICustomerRepository repository;

    ModelMapper mapper;

    UpdateCustomerUseCase updateCustomerUseCase;

    @BeforeEach
    void setUp() {
        mapper = new ModelMapper();
        updateCustomerUseCase = new UpdateCustomerUseCase(repository, mapper);
    }

    @Test
    @DisplayName("updateCustomer_Success")
    void updateCustomer(){

        var customer = new Customer("Juan", "Nieves",
                "jn23@gmail.com", "31289746987");
        customer.setId("1");

        var monoCustomer = Mono.just(customer);

        Mockito.when(repository.findById(Mockito.any(String.class))).thenReturn(monoCustomer);

        var customerUpdated = new Customer("Juan", "Rubiano",
                "juanr90@gmail.com", "31289746987");
        customerUpdated.setId("1");

        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(Mono.just(customerUpdated));

        var result = updateCustomerUseCase.update("1",
                mapper.map(customerUpdated, CustomerDTO.class));

        StepVerifier.create(result)
                .expectNext(mapper.map(customerUpdated, CustomerDTO.class))
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).findById("1");
        Mockito.verify(repository, times(1)).save(customerUpdated);
    }

    @Test
    @DisplayName("updateCustomer_Failed")
    void updateCustomer_Failed(){
        var customerUpdated = new Customer("Juan", "Rubiano",
                "juanr90@gmail.com", "31289746987");
        customerUpdated.setId("1");

        Mockito.when(repository.findById(Mockito.any(String.class)))
                .thenReturn(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())));

        var result = updateCustomerUseCase.update("1",
                mapper.map(customerUpdated, CustomerDTO.class));

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable != null &&
                        throwable.getMessage().equals(HttpStatus.NOT_FOUND.toString()))
                .verify();

        Mockito.verify(repository).findById("1");
        Mockito.verify(repository, never()).save(customerUpdated);
    }
}