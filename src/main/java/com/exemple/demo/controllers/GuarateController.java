package com.exemple.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemple.demo.entities.Garante;
import com.exemple.demo.repositories.GuarateRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")

@CrossOrigin("*")
@RequiredArgsConstructor
public class GuarateController {

    private final GuarateRepository guarateRepository;

    @PostMapping("/guarante")
    // @Secured("ROLE_ADMIN")
    ResponseEntity<?> saveGuarante(@RequestBody Garante garante) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guarateRepository.save(garante));
    }

    @RequestMapping("/guarante")
    ResponseEntity<?> getGurante() {
        return ResponseEntity.ok(guarateRepository.findAll());
    }

}
