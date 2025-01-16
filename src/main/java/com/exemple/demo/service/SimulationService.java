package com.exemple.demo.service;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.exemple.demo.repositories.SimulationRepository;

import lombok.RequiredArgsConstructor;

@Service
// @Component
@RequiredArgsConstructor
public class SimulationService {

    private final SimulationRepository simulationRepository;

    // génération du code de 12 caractères
    public String generateQuote() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

}
