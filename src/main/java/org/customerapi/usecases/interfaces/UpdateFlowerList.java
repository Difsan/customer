package org.customerapi.usecases.interfaces;

import org.customerapi.domain.dto.CustomerDTO;
import org.customerapi.domain.flower.Flower;
import reactor.core.publisher.Mono;

public interface UpdateFlowerList{

    Mono<CustomerDTO> add(String customerId, Flower flower);
    Mono <CustomerDTO> remove(String customerId, Flower flower);
}
