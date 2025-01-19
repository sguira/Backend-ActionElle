package com.exemple.demo.service;

import java.sql.Date;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exemple.demo.config.JwtUtils;
import com.exemple.demo.entities.Utilisateur;
import com.exemple.demo.repositories.UtilisateurRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceCustom userDetailsServiceCustom;

    public Utilisateur registerUser(Utilisateur utilisateur) throws Exception {

        Utilisateur u = utilisateurRepository.findUserByUsername(utilisateur.getUsername());

        if (u != null) {
            throw new Exception("Username already exists");
        }
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        utilisateur.setCreatedAt((new Date(System.currentTimeMillis())).toString());
        utilisateur.setRoles("ROLE_AMAZONE");
        return utilisateurRepository.save(utilisateur);
    }

    public List<Utilisateur> getAllUsers() {
        return utilisateurRepository.findAll();
    }

    // une méthode qui retourne le nom d'utilsateur à partir du token
    public String getuserIdByToken(String token) throws Exception {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtUtils.extractUsername(token);
            if (jwtUtils.validateToken(token, userDetailsServiceCustom.loadUserByUsername(username))) {
                return utilisateurRepository.findUserByUsername(username).getId();
            } else {
                throw new Exception("Token non valide");
            }
        }
        throw new Exception("Format token invalide");
    }

}
