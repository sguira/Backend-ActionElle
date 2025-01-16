package com.exemple.demo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Assuree {

    @Id
    private String id;
    private String nom;
    private String adresse;
    private String telephone;
    private String cin;
    private String ville;
    private TypeClient type = TypeClient.PROSPECT;

}

// enum TypeClient {
// PROSPECT,
// CLIENT
// }
