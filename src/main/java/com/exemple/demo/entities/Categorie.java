package com.exemple.demo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// import jakarta.persistence.Id;
import lombok.Data;

@Data
@Document
public class Categorie {

    @Id
    private String id;
    private String code;

    private String libelle;
    private String description;

}
