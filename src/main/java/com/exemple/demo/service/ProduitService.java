package com.exemple.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.exemple.demo.entities.ProduitAssure;
import com.exemple.demo.repositories.ProduitAssureeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProduitService {

    private final ProduitAssureeRepository produitAssureeRepository;

    // service qui retourne la liste de tous les produits
    public ResponseEntity<List<ProduitAssure>> getAllProduits() {
        try {
            return ResponseEntity.ok(produitAssureeRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // service qui retourne un seul produit
    public ResponseEntity<ProduitAssure> getProduits(String id) {
        try {
            return ResponseEntity.ok(produitAssureeRepository.findById(id).get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // service pour effacer un produit
    public ResponseEntity<?> deleteProduct(String id) {
        try {
            produitAssureeRepository.deleteById(id);
            return (ResponseEntity) ResponseEntity.status(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // service pour ajouter un produit
    public ResponseEntity<ProduitAssure> saveProduct(ProduitAssure produit) {
        try {

            return ResponseEntity.ok(produitAssureeRepository.save(produit));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
