package com.sofka.customerservice.repository;

import com.sofka.customerservice.domain.OutboxEvent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findTop50ByStatusInOrderByIdAsc(List<String> statuses);
}
