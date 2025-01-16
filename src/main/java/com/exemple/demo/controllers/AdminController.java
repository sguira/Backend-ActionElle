package com.exemple.demo.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import com.exemple.demo.entities.Categorie;
import com.exemple.demo.entities.ProduitAssure;
import com.exemple.demo.repositories.CategorieRepository;
import com.exemple.demo.repositories.ProduitAssureeRepository;
import com.exemple.demo.repositories.UtilisateurRepository;
import com.exemple.demo.service.UtilisateurService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@CrossOrigin("*")
public class AdminController {

    private final UtilisateurService utilisateurService;
    private final CategorieRepository categorieRepository;
    private final ProduitAssureeRepository produitAssureeRepository;

    @GetMapping(path = "/users")
    @Secured("ROLE_ADMIN")
    ResponseEntity<?> getUser() {
        System.out.println("Appelé\n\n");
        try {
            System.out.println("Appelé\n\n");
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

    // méthode pour ajouter une nouvelle categorie
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

    // ajouter un nouveau produit
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

}