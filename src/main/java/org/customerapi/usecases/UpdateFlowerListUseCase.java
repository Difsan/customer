package org.customerapi.usecases;

import lombok.RequiredArgsConstructor;
import org.customerapi.domain.collection.Customer;
import org.customerapi.domain.dto.CustomerDTO;
import org.customerapi.domain.flower.Flower;
import org.customerapi.repository.ICustomerRepository;
import org.customerapi.usecases.interfaces.UpdateCustomer;
import org.customerapi.usecases.interfaces.UpdateFlowerList;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateFlowerListUseCase implements UpdateFlowerList {

    private final ICustomerRepository customerRepository;

    private final ModelMapper mapper;


    @Override
    public void add(String customerId, Flower flower) {
        this.customerRepository
                .findById(customerId)
                //.switchIfEmpty(Mono.empty())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("There is not " +
                        "customer with id: " + customerId)))
                .flatMap(customer -> {
                    var listOfFlowers = customer.getFlowers();
                    /*listOfFlowers.stream().forEach(flower1 -> {
                        if (flower1.getId().equals(flower.getId())) Mono.error(new Throwable("Flower already bought"));
                    });*/
                    listOfFlowers.add(flower);
                    customer.setFlowers(listOfFlowers);
                    return this.customerRepository.save(customer);
                })
                .map(customer -> mapper.map(customer, CustomerDTO.class)).subscribe();
    }

    @Override
    public Mono<CustomerDTO> remove(String customerId, Flower flower) {
        return null;
    }
}
