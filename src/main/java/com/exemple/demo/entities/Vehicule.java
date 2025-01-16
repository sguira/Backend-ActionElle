package com.exemple.demo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
// import jakarta.persistence.Id;
import lombok.Data;

@Data
@Document

public class Vehicule {

    @Id
    private String id;
    private String name;
    // private String model;
    private String color;
    private String immatriculation;

    @NotBlank(message = "Cette valeur ne peux pas être vide")
    private String dateMiseCirculation;
    private int nombreSieges;
    private int nombresPortes;

    private String modele;

    private double valeurVenale;
    private double prixDachat;
    private String categorie;
    private int puissance;

}
