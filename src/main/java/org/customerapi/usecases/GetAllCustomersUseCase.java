package org.customerapi.usecases;


import lombok.RequiredArgsConstructor;
import org.customerapi.domain.dto.CustomerDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import org.customerapi.repository.ICustomerRepository;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class GetAllCustomersUseCase implements Supplier<Flux<CustomerDTO>> {

    private final ICustomerRepository customerRepository;
    private final ModelMapper mapper;

    @Override
    public Flux<CustomerDTO> get() {
        return this.customerRepository
                .findAll()
                .switchIfEmpty(Flux.empty())
                .map(customer -> mapper.map(customer, CustomerDTO.class));
    }
}
