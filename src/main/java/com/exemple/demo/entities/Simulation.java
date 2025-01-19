package com.exemple.demo.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Document
// @RequiredArgsConstructor
public class Simulation {

    @Id
    private String id;

    @DBRef
    private ProduitAssure produitAssure;

    @DBRef
    private Categorie categorie;
    private String dateDebut;
    private String dateFin;

    @NotBlank(message = "cette valeur est obligatoire")
    private String miseCirculation;

    @NotNull(message = "cette valeur ne doit pas être vide")
    private double valeurVenale;
    private double valeurNeuf;
    private int puissance;
    private String userId;
    private Map<String, ?> details = new HashMap<>();
    private String createdAt = (new Date(System.currentTimeMillis())).toString();
}
