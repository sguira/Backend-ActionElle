package com.exemple.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemple.demo.entities.Categorie;
import com.exemple.demo.repositories.CategorieRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@CrossOrigin("*")
public class CategorieController {
    private final CategorieRepository categorieRepository;

    @RequestMapping(path = "/categorie")
    ResponseEntity<?> getCategorie() {
        return new ResponseEntity<>(categorieRepository.findAll(), HttpStatus.OK);
    }
}
