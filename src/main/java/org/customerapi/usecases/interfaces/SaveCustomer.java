package org.customerapi.usecases.interfaces;

import org.customerapi.domain.dto.CustomerDTO;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface SaveCustomer {
    Mono<CustomerDTO> save(CustomerDTO customerDTO);
}
