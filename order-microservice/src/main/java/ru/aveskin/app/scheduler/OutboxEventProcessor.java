package ru.aveskin.app.scheduler;

public interface OutboxEventProcessor {
    void processOutboxEvents();
}
