package com.exemple.demo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.exemple.demo.entities.Assuree;

public interface AssureeRepository extends MongoRepository<Assuree, String> {

}
