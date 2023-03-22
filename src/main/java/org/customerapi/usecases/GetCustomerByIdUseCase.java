package org.customerapi.usecases;

import com.mongodb.Function;
import lombok.RequiredArgsConstructor;
import org.customerapi.domain.dto.CustomerDTO;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.customerapi.repository.ICustomerRepository;

@Service
@RequiredArgsConstructor
public class GetCustomerByIdUseCase implements Function<String, Mono<CustomerDTO>> {

    private final ICustomerRepository customerRepository;
    private final ModelMapper mapper;

    @Override
    public Mono<CustomerDTO> apply(String id) {
        return this.customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .map(customer -> mapper.map(customer, CustomerDTO.class));
    }
}
