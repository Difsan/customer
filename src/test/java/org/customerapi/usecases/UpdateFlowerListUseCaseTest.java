package org.customerapi.usecases;

import org.customerapi.domain.collection.Customer;
import org.customerapi.domain.flower.Flower;
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

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UpdateFlowerListUseCaseTest {

    @Mock
    private ICustomerRepository repository;

    private ModelMapper mapper;

    private UpdateFlowerListUseCase updateFlowerListUseCase;

    @BeforeEach
    void setUp() {
        mapper = new ModelMapper();
        updateFlowerListUseCase = new UpdateFlowerListUseCase(repository, mapper);
    }

    @Test
    @DisplayName("updateFlowerListSuccessfully")
    void updateFlowerList(){

        // flower to be added
        var flower = new Flower("flowerID", "Daisy", "Asteraceae",
                "white", "crown", "Mediterranean coast", false);

        // customer found by ID
        Customer customer = new Customer("Pablo", "Pira",
                "pp@gmail.com", "32186987456");
        customer.setId("1");
        //customer.setFlowers(new HashSet<>());

        Mockito.when(repository.findById("1")).thenReturn(Mono.just(customer));

        // List of flowers to be updated
        var flowers = customer.getFlowers();
        flowers.add(flower);

        // customer updated
        Customer customerUpdated = new Customer("Pablo", "Pira",
                "pp@gmail.com", "32186987456");
        customerUpdated.setId("1");
        customerUpdated.setFlowers(flowers);

        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(Mono.just(customerUpdated));

        updateFlowerListUseCase.add("1", flower);

        StepVerifier.create(repository.save(customerUpdated))
                .expectNext(customerUpdated)
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).findById("1");
        Mockito.verify(repository, times(2)).save(customerUpdated);
    }

    @Test
    @DisplayName("updateFlowerListFailed")
    void updateFlowerList_Failed(){
        // flower to be added
        var flower = new Flower("flowerID", "Daisy", "Asteraceae",
                "white", "crown", "Mediterranean coast", false);

        String customerId = "2";

        Mockito.when(repository.findById(customerId))
                .thenReturn(Mono.error(new IllegalArgumentException("There is not " +
                        "customer with id: " + customerId)));

        updateFlowerListUseCase.add(customerId, flower);

        StepVerifier.create(repository.findById(customerId))
                .expectErrorMatches(throwable -> throwable != null &&
                        throwable.getMessage().equals("There is not " +
                                "customer with id: " + customerId))
                .verify();

        Mockito.verify(repository, times(2)).findById(customerId);

        Mockito.verify(repository, never()).save(Mockito.any(Customer.class));
    }
}