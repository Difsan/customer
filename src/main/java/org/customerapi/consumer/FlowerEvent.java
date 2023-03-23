package org.customerapi.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.customerapi.domain.flower.Flower;

@Data
@AllArgsConstructor
public class FlowerEvent {
    private String customerId;
    private Flower flowerBought;
    private String eventType;
}
