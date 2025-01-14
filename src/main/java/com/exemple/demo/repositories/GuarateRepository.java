package com.exemple.demo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.exemple.demo.entities.Garante;

public interface GuarateRepository extends MongoRepository<Garante, String> {

}
