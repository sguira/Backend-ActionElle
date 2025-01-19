package com.exemple.demo.entities;

import java.sql.Date;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Size;
// import jakarta.persistence.Id;
import lombok.Data;

@Document
@Data
public class Utilisateur {

    @Id
    private String id;

    @NotNull(message = "Username is required")
    @Email(message = "doit ête un email")
    private String username;
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractère")
    private String password;

    private String name;
    private String telephone;
    private String ville;
    // @DBRef
    private String roles;
    private String createdAt = new Date(System.currentTimeMillis()).toString();
    @NotNull(message = "champ requis")
    private String cin;

}
