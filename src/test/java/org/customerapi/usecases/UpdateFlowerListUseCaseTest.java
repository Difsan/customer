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
    @DisplayName("updateFlowerList_AddFlowerSuccessfully")
    void updateFlowerList_AddFlower(){

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
    @DisplayName("updateFlowerList_RemoveFlowerSuccessfully")
    void updateFlowerList_RemoveFlower(){

        // flowers already added in customer
        var flower = new Flower("flowerID", "Daisy", "Asteraceae",
                "white", "crown", "Mediterranean coast", false);
        var flower2 =  new Flower("flowerID2","Rose" , "Rosaceae",
                "pink", "Virginia rose", "Peru", false);

        // customer found by ID
        Customer customer = new Customer("Pablo", "Pira",
                "pp@gmail.com", "32186987456");
        customer.setId("1");
        //add the flower to the customer, that's how we get the customer from dindByID
        var flowers = customer.getFlowers();
        flowers.add(flower);
        flowers.add(flower2);
        customer.setFlowers(flowers);

        Mockito.when(repository.findById("1")).thenReturn(Mono.just(customer));

        // flower received from publisher
        var flowerUpdated = new Flower("flowerID", "Daisy", "Asteraceae",
                "white", "crown", "Mediterranean coast", true);

        // remove flower from user
        var customerUpdated = customer;
        var flowersChanged = customerUpdated.getFlowers();
        flowersChanged.remove(flower);
        customerUpdated.setFlowers(flowersChanged);

        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(Mono.just(customerUpdated));

        updateFlowerListUseCase.remove("1", flowerUpdated);

        StepVerifier.create(repository.save(customerUpdated))
                .expectNext(customerUpdated)
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).findById("1");
        Mockito.verify(repository, times(2)).save(customerUpdated);

    }

    @Test
    @DisplayName("updateFlowerList_AddFlowerFailed")
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

    @Test
    @DisplayName("updateFlowerList_RemoveFlowerFailed")
    void updateFlowerList_RemoveFailed(){
        // flower received from publisher
        var flowerUpdated = new Flower("flowerID", "Daisy", "Asteraceae",
                "white", "crown", "Mediterranean coast", true);

        String customerId = "2";

        Mockito.when(repository.findById(customerId))
                .thenReturn(Mono.error(new IllegalArgumentException("There is not " +
                        "customer with id: " + customerId)));

        updateFlowerListUseCase.add(customerId, flowerUpdated);

        StepVerifier.create(repository.findById(customerId))
                .expectErrorMatches(throwable -> throwable != null &&
                        throwable.getMessage().equals("There is not " +
                                "customer with id: " + customerId))
                .verify();

        Mockito.verify(repository, times(2)).findById(customerId);

        Mockito.verify(repository, never()).save(Mockito.any(Customer.class));
    }
}