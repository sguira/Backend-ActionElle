package com.exemple.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemple.demo.config.JwtUtils;
import com.exemple.demo.entities.Assuree;
import com.exemple.demo.entities.Subscription;
import com.exemple.demo.entities.Utilisateur;
import com.exemple.demo.entities.Vehicule;
import com.exemple.demo.repositories.AssureeRepository;
import com.exemple.demo.repositories.SubscriptionRepository;
import com.exemple.demo.repositories.UtilisateurRepository;
import com.exemple.demo.repositories.VehiculeRepository;
import com.exemple.demo.service.UserDetailsServiceCustom;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SousCriptionController {

    private final SubscriptionRepository subscriptionRepository;
    private final AssureeRepository assureeRepository;
    private final VehiculeRepository vehiculeRepository;
    private final JwtUtils jwtUtils;
    private final UtilisateurRepository utilisateurRepository;
    private final UserDetailsServiceCustom userDetailsServiceCustom;
    // private final AssureeRepository

    // méthode qui permet de faire une souscription
    @PostMapping("/souscriptions")
    @Transactional
    public ResponseEntity<?> createSubscription(@RequestBody Subscription subscription,
            @RequestHeader("Authorization") String token) {

        try {
            if (token.startsWith("Bearer ")) {

                // recuperation de l'amazone qui veut faire la suscription à laide de son token
                token = token.substring(7);
                String username = jwtUtils.extractUsername(token);
                String amazoneId = utilisateurRepository.findUserByUsername(username).getId();

                // recupère les info concernant le vehicule puis l'enregistrer dans la table des
                // vehicule
                Vehicule vehicule = subscription.getVehicule();

                // Assuree assuree = subscription.getAssuree();
                subscription.setAmazoneId(amazoneId);
                subscription.setVehicule(vehiculeRepository.save(vehicule));

                // enregistrement de l'assuré
                subscription.setAssuree(assureeRepository.save(subscription.getAssuree()));
                return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionRepository.save(subscription));
            } else {
                System.out.println("Non autorisé");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Invalid");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    // une méthode qui retourne le nom d'utilsateur à partir du token
    String getuserIdByToken(String token) throws Exception {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtUtils.extractUsername(token);
            if (jwtUtils.validateToken(token, userDetailsServiceCustom.loadUserByUsername(username))) {
                return utilisateurRepository.findUserByUsername(username).getId();
            } else {
                throw new Exception("Token non valide");
            }
        }
        throw new Exception("Format token invalide");
    }

    // retourne la liste de toutes les souscription par Utilisateur
    @GetMapping("/souscriptions")
    public ResponseEntity<?> getAllSubscriptionByUser(@RequestHeader("Authorization") String token) {
        System.out.println("Appelé\n\n");
        try {
            String amazoneId = getuserIdByToken(token);
            return ResponseEntity.ok(subscriptionRepository.findByAmazoneId(amazoneId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
