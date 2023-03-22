package org.customerapi.usecases;

import lombok.RequiredArgsConstructor;
import org.customerapi.domain.collection.Customer;
import org.customerapi.domain.dto.CustomerDTO;
import org.customerapi.usecases.interfaces.SaveCustomer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.customerapi.repository.ICustomerRepository;

@Service
@RequiredArgsConstructor
public class SaveCustomerUseCase implements SaveCustomer {

    private final ICustomerRepository customerRepository;

    private final ModelMapper mapper;

    @Override
    public Mono<CustomerDTO> save(CustomerDTO customerDTO) {
        return this.customerRepository
                .save(mapper.map(customerDTO, Customer.class))
                .switchIfEmpty(Mono.empty())
                .map(customer -> mapper.map(customer, CustomerDTO.class));
    }
}
