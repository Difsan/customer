package org.customerapi.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.customerapi.usecases.UpdateFlowerListUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomerConsumer {

    private final ObjectMapper objectMapper;

    private final UpdateFlowerListUseCase updateFlowerListUseCase;

    @RabbitListener(queues = "flowers.queue")
    public void receiveEventFlower(String message) throws JsonProcessingException{
        FlowerEvent event = objectMapper.readValue(message, FlowerEvent.class);
        if (event.getEventType().equals("buy")){
            updateFlowerListUseCase.add(event.getCustomerId(), event.getFlowerBought());
        } else if (event.getEventType().equals("return")){
            updateFlowerListUseCase.remove(event.getCustomerId(), event.getFlowerBought());
        }
    }
}
