package com.sofka.customerservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_event")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregate_type", nullable = false, length = 50)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false)
    private Long aggregateId;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "attempts", nullable = false)
    private Integer attempts;

    @Column(name = "last_error", length = 500)
    private String lastError;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    public static OutboxEvent pending(String aggregateType, Long aggregateId, String eventType, String payload) {
        OutboxEvent event = new OutboxEvent();
        event.aggregateType = aggregateType;
        event.aggregateId = aggregateId;
        event.eventType = eventType;
        event.payload = payload;
        event.status = "PENDING";
        event.attempts = 0;
        event.createdAt = LocalDateTime.now();
        return event;
    }

    public void markPublished() {
        this.status = "PUBLISHED";
        this.publishedAt = LocalDateTime.now();
        this.lastError = null;
    }

    public void markFailed(String errorMessage, int maxAttempts) {
        this.attempts = this.attempts + 1;
        this.lastError = errorMessage == null ? null : errorMessage.substring(0, Math.min(errorMessage.length(), 500));
        this.status = this.attempts >= maxAttempts ? "DEAD" : "FAILED";
    }

    public Long getId() {
        return id;
    }

    public String getPayload() {
        return payload;
    }
}
