package com.exemple.demo.repositories;

import java.util.List;

// import java.util.concurrent.Flow.Subscription;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.exemple.demo.entities.Subscription;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    public List<Subscription> findByAmazoneId(String amazoneId);

}
