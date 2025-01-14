package com.exemple.demo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.exemple.demo.entities.Categorie;

public interface CategorieRepository extends MongoRepository<Categorie, String> {

}
