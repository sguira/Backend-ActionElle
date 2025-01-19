package com.exemple.demo.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.exemple.demo.entities.Simulation;

public interface SimulationRepository extends MongoRepository<Simulation, String> {

    public List<Simulation> findByUserId(String id);

}
