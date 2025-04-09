package ru.aveskin.app.scheduler.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aveskin.app.event.PaymentCreatedEvent;
import ru.aveskin.app.model.OutboxEvent;
import ru.aveskin.app.model.OutboxStatus;
import ru.aveskin.app.repository.OutboxRepository;
import ru.aveskin.app.scheduler.OutboxEventProcessor;

import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxEventProcessorImpl implements OutboxEventProcessor {
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, PaymentCreatedEvent> kafkaTemplate;

    @Value("${outbox.processor.batch-size:10}")
    private int batchSize;

    @Value("${outbox.processor.enabled:true}")
    private boolean enabled;

    @Scheduled(fixedDelayString = "${outbox.processor.fixed-delay:5000}")
    @Transactional
    @Override
    public void processOutboxEvents() {
        if (!enabled) {
            return;
        }
        log.info("Polling for new outbox events...");

        PageRequest pageable = PageRequest.of(0, batchSize, Sort.by("createdAt").ascending());
        List<OutboxEvent> events = outboxRepository.findByStatus(OutboxStatus.NEW, pageable);

        if (events.isEmpty()) {
            log.info("No new outbox events found.");
            return;
        }

        log.info("Found {} new outbox events to process.", events.size());

        for (OutboxEvent event : events) {

            log.info("Попытка отправки в Kafka: {}", event.getPayload());

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                PaymentCreatedEvent paymentCreatedEvent = objectMapper.readValue(event.getPayload(), PaymentCreatedEvent.class);

                SendResult<String, PaymentCreatedEvent> result = kafkaTemplate
                        .send("payment-created-event-topic", event.getId().toString(), paymentCreatedEvent).get();

                log.info("Topic " + result.getRecordMetadata().topic());
                log.info("Partition " + result.getRecordMetadata().partition());
                log.info("Offset " + result.getRecordMetadata().offset());
                log.info("Timestamp " + result.getRecordMetadata().timestamp());

                markEventAsProcessed(event);

            } catch (Exception e) {
                log.error("Error processing event ID: {}. Error: {}", event.getId(), e.getMessage(), e);
                markEventAsFailed(event); // Помечаем как FAILED при других ошибках
            }
        }
    }

    // @Transactional(propagation = Propagation.REQUIRES_NEW) // если нужно гарантировать коммит статуса независимо от основной транзакции
    private void markEventAsProcessed(OutboxEvent event) {
        event.setStatus(OutboxStatus.PROCESSED);
        event.setCreatedAt(Instant.now());
        outboxRepository.save(event);
    }

    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void markEventAsFailed(OutboxEvent event) {
        event.setStatus(OutboxStatus.FAILED);
        event.setCreatedAt(Instant.now());
        outboxRepository.save(event);
        // Возможно, нужна логика для Dead Letter Queue или алертов
    }

}

