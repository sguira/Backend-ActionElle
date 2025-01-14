package com.exemple.demo.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
// import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Document
@RequiredArgsConstructor
public class ProduitAssure {

    @Id
    private String id;
    @NotNull
    private String nomProduit;
    @DBRef
    private List<Garante> garanties;
    @DBRef
    private List<Categorie> categoriesElligibles;

}
