package com.exemple.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemple.demo.entities.Assuree;
import com.exemple.demo.entities.Garante;
import com.exemple.demo.entities.ProduitAssure;
import com.exemple.demo.entities.Simulation;
import com.exemple.demo.entities.Subscription;
import com.exemple.demo.repositories.AssureeRepository;
import com.exemple.demo.repositories.CategorieRepository;
import com.exemple.demo.repositories.GuarateRepository;
import com.exemple.demo.repositories.ProduitAssureeRepository;
import com.exemple.demo.repositories.SimulationRepository;
import com.exemple.demo.repositories.SubscriptionRepository;
import com.exemple.demo.service.UtilisateurService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final ProduitAssureeRepository produitAssureRepository;
    private final GuarateRepository guarateRepository;
    private final CategorieRepository categorieRepository;
    private final AssureeRepository assureeRepository;
    private final UtilisateurService utilisateurService;
    // private final SimulationRepository simulationRepository;
    private final SubscriptionRepository subscriptionRepository;

    // retourne la liste des produits
    @GetMapping("/products")
    public ResponseEntity<List<ProduitAssure>> getAllProducts() {
        return ResponseEntity.ok(produitAssureRepository.findAll());
    }

    // retourne la liste des garanties
    @RequestMapping("/guarante")
    ResponseEntity<?> getGurante() {
        return ResponseEntity.ok(guarateRepository.findAll());
    }

    // retourne la liste des categories
    @RequestMapping(path = "/categorie")
    ResponseEntity<?> getCategorie() {
        return new ResponseEntity<>(categorieRepository.findAll(), HttpStatus.OK);
    }

    // retourne la liste des clients par utilisateurs
    @GetMapping("/clients")
    public ResponseEntity<Object> getAllClientsByUser(@RequestHeader("Authorization") String token)
            throws Exception {
        String userId = utilisateurService.getuserIdByToken(token);
        List<Assuree> assurees = new ArrayList<>();
        List<Subscription> subscription = subscriptionRepository.findByAmazoneId(userId);
        subscription.forEach(element -> {
            boolean exist = false;
            for (Assuree assuree : assurees) {
                if (assuree.getId().equals(element.getAssuree().getId())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                assurees.add(element.getAssuree());
            }
        });

        return ResponseEntity.ok(assurees);
    }

}
