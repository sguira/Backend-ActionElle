package com.exemple.demo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.exemple.demo.entities.Vehicule;

public interface VehiculeRepository extends MongoRepository<Vehicule, String> {

}
