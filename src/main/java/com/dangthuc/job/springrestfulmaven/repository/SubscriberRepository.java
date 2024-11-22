package com.dangthuc.job.springrestfulmaven.repository;

import com.dangthuc.job.springrestfulmaven.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long>,
        JpaSpecificationExecutor<Subscriber> {
    boolean existsByEmail(String email);

    Subscriber findByEmail(String email);
}