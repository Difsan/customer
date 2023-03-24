package org.customerapi.usecases.interfaces;

import org.customerapi.domain.dto.CustomerDTO;
import org.customerapi.domain.flower.Flower;
import reactor.core.publisher.Mono;

public interface UpdateFlowerList{

    void add(String customerId, Flower flower);
    void remove(String customerId, Flower flower);
}
