package org.customerapi.usecases;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.customerapi.repository.ICustomerRepository;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class DeleteCustomerUseCase implements Function<String, Mono<Void>> {

    private final ICustomerRepository customerRepository;
    private final ModelMapper mapper;


    @Override
    public Mono<Void> apply(String id) {
        return this.customerRepository
                .findById(id)
                //.switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("There is not " +
                        "customer with id: " + id)))
                .flatMap(customer -> this.customerRepository.deleteById(customer.getId()));
    }
}
