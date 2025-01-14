package com.exemple.demo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
// import org.springframework.data.rest.core.annotation.RestResource;

import com.exemple.demo.entities.Utilisateur;

// @RestResource
public interface UtilisateurRepository extends MongoRepository<Utilisateur, String> {

    Utilisateur findUserByUsername(String user);

}
