package org.customerapi.usecases;

import lombok.RequiredArgsConstructor;
import org.customerapi.domain.collection.Customer;
import org.customerapi.domain.dto.CustomerDTO;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.customerapi.repository.ICustomerRepository;
import org.customerapi.usecases.interfaces.UpdateCustomer;

@Service
@RequiredArgsConstructor
public class UpdateCustomerUseCase implements UpdateCustomer {

    private final ICustomerRepository customerRepository;

    private final ModelMapper mapper;

    @Override
    public Mono<CustomerDTO> update(String id, CustomerDTO customerDTO) {
        return this.customerRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("There is not " +
                        "customer with id: " + id)))
                .flatMap(customer -> {
                    customerDTO.setId(customerDTO.getId());
                    return customerRepository.save(mapper.map(customerDTO, Customer.class));
                })
                .map(customer -> mapper.map(customer, CustomerDTO.class));
    }
}
