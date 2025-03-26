package ru.aveskin.ordermicroservice.order.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.aveskin.ordermicroservice.order.dto.OrderPaymentEventRequest;
import ru.aveskin.ordermicroservice.order.service.KafkaEventSender;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaEventSenderImpl implements KafkaEventSender {
    private final KafkaTemplate<String, OrderPaymentEventRequest> kafkaTemplateOrderPaymentTriggered;
    private final TransactionTemplate transactionTemplate;

    @Override
    public void sendPaymentRequestEvent(OrderPaymentEventRequest orderPaymentEventRequest) {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            String orderId = orderPaymentEventRequest.getOrderId().toString();
            sendMessage(kafkaTemplateOrderPaymentTriggered, "payment-request-events-topic", orderId, orderPaymentEventRequest);
        });
    }

    private <T> void sendMessage(KafkaTemplate<String, T> kafkaTemplate, String topic, String key, T payload) {
        kafkaTemplate.executeInTransaction(operations -> {
            operations.send(topic, key, payload);
            return null;
        });
    }
}

