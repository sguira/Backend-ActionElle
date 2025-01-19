package com.exemple.demo.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exemple.demo.entities.Categorie;
import com.exemple.demo.entities.Garante;
import com.exemple.demo.entities.ProduitAssure;
import com.exemple.demo.entities.Simulation;
import com.exemple.demo.entities.Subscription;
import com.exemple.demo.entities.Utilisateur;
import com.exemple.demo.repositories.AssureeRepository;
import com.exemple.demo.repositories.CategorieRepository;
import com.exemple.demo.repositories.GuarateRepository;
import com.exemple.demo.repositories.ProduitAssureeRepository;
import com.exemple.demo.repositories.SimulationRepository;
import com.exemple.demo.repositories.SubscriptionRepository;
import com.exemple.demo.repositories.UtilisateurRepository;
import com.exemple.demo.service.UtilisateurService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@CrossOrigin("*")
@Hidden
public class AdminController {

    private final UtilisateurService utilisateurService;
    private final UtilisateurRepository utilisateurRepository;
    private final CategorieRepository categorieRepository;
    private final ProduitAssureeRepository produitAssureeRepository;
    private final AssureeRepository assureeRepository;
    private final GuarateRepository guarateRepository;
    private final SimulationRepository simulationRepository;
    private final SubscriptionRepository subscriptionRepository;

    // cette méthode retourne la liste des utilisateurs uniquement diponible pour
    // les administrateur
    @GetMapping(path = "/users")
    @Secured("ROLE_ADMIN")
    ResponseEntity<?> getUser() {

        try {

            return ResponseEntity.status(HttpStatus.OK).body(utilisateurService.getAllUsers());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error inconnu" + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // méthode pour ajouter une nouvelle categorie
    @PostMapping("/categorie")
    public ResponseEntity<?> addCategorie(@RequestBody Categorie categorie) {
        System.out.println("\n\n Ajout de categorie \n\n\n");
        if (categorie.getCode() != null && categorie.getLibelle() != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(categorieRepository.save(categorie));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verifiez les champs");
    }

    // méthode qui retourne la liste des categoriees
    @GetMapping("/categorie")
    public ResponseEntity<?> getCategorie() {

        return ResponseEntity.status(HttpStatus.OK).body(categorieRepository.findAll());

    }

    // ajouter un nouveau produit
    @PostMapping("/produit")
    ResponseEntity<?> saveProduct(@RequestBody ProduitAssure produit) {

        // System.out.print("Nom Categorie" + produit.getGaranties().get(0).getName() +
        // "\n\n\n");

        return ResponseEntity.status(HttpStatus.CREATED).body(produitAssureeRepository.save(produit));
    }

    // Retourne la liste des produit disponible
    @GetMapping("/produit")
    ResponseEntity<?> getProduct() {

        // System.out.print("Nom Categorie" + produit.getGaranties().get(0).getName() +
        // "\n\n\n");

        return ResponseEntity.status(HttpStatus.CREATED).body(produitAssureeRepository.findAll());
    }

    // effacer un produit
    @DeleteMapping("/produit/{id}")
    ResponseEntity<?> deleteProduit(@PathVariable String id) {
        produitAssureeRepository.deleteById(id);
        return ResponseEntity.ok("Elment supprimé");
    }

    // La liste des souscripteurs (assuré)
    @GetMapping("/souscripteurs")
    public ResponseEntity<?> getSouscripteur() {
        return ResponseEntity.status(HttpStatus.OK).body(assureeRepository.findAll());
    }

    // ajouter une garantie
    @PostMapping("/guarante")
    // @Secured("ROLE_ADMIN")
    ResponseEntity<?> saveGuarante(@RequestBody Garante garante) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guarateRepository.save(garante));
    }

    // méthode qui permet de retourner la liste des souscriptions pour un
    // utilisateurs donné
    @RequestMapping("/souscriptions")
    public ResponseEntity<?> getSubscriptions(@RequestParam("amazoneId") String id) {
        try {
            List<Subscription> souscriptions = subscriptionRepository.findByAmazoneId(id);
            Utilisateur utilisateur = utilisateurRepository.findById(id).get();
            double primeTotal = 0;
            for (Subscription simulation : souscriptions) {
                try {
                    primeTotal += (double) simulation.getDetails().get("Montant Prime");
                } catch (Exception e) {

                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("souscriptions", souscriptions);
            result.put("primeTotal", primeTotal);
            result.put("amazone", utilisateur);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Aucun element trouvé");
        }
    }

    // méthode qui permet de retourner la liste des simulations pour un
    // utilisateurs
    @RequestMapping("/simulations")
    public ResponseEntity<?> getSimulations(@RequestParam("id") String id) {
        try {
            List<Simulation> simulations = simulationRepository.findByUserId(id);
            Utilisateur utilisateur = utilisateurRepository.findById(id).get();
            double primeTotal = 0;

            for (Simulation simulation : simulations) {
                primeTotal += (double) simulation.getDetails().get("Montant Prime");
            }
            Map<String, Object> result = new HashMap<>();
            result.put("simulations", simulations);
            result.put("amazone", utilisateur);
            result.put("primeTotal", primeTotal);

            return ResponseEntity.ok(simulations);
        } catch (Exception e) {
            return ResponseEntity.ok("Aucun element trouvé");
        }
    }

}