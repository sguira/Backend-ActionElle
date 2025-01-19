package com.exemple.demo.entities;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Document
@NoArgsConstructor
// @RequiredArgsConstructor
@AllArgsConstructor
public class Subscription {
    @Id
    private String id;

    private String quoteReference;
    @DBRef
    private Vehicule vehicule;
    @DBRef
    private Assuree assuree;

    // @NotNull
    private String amazoneId;

    @DBRef
    private ProduitAssure produitAssure;
    private String urlAttestation;
    private String status;
    private String dateCreated = (new Date(System.currentTimeMillis())).toString();

    private Map<String, Object> details;
}
