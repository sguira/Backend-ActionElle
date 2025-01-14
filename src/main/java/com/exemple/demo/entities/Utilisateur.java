package com.exemple.demo.entities;

import java.sql.Date;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

// import jakarta.persistence.Id;
import lombok.Data;

@Document
@Data
public class Utilisateur {

    @Id
    private String id;

    private String username;
    private String password;
    private String name;
    private String telephone;
    private String ville;
    // @DBRef
    private String roles;
    private String createdAt;

    private String cin;

}
