package ru.aveskin.app.repository;

import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import ru.aveskin.app.model.OutboxEvent;
import ru.aveskin.app.model.OutboxStatus;

import java.util.List;
import java.util.Optional;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {

    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "1000"))
    List<OutboxEvent> findByStatus(OutboxStatus status, Pageable pageable);


    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "false"))
    @Query("SELECT o FROM OutboxEvent o WHERE o.orderId = :orderId")
    Optional<OutboxEvent> findByOrderId(@Param("orderId") Long orderId);
}
