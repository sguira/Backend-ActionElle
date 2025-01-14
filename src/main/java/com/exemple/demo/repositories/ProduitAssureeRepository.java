package com.exemple.demo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.exemple.demo.entities.ProduitAssure;

public interface ProduitAssureeRepository extends MongoRepository<ProduitAssure, String> {

}
