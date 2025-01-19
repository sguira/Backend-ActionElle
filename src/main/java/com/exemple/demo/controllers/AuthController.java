package com.exemple.demo.controllers;

import java.util.HashMap;
import java.util.Map;
// import java.util.logging.Logger;

// import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exemple.demo.config.JwtUtils;
import com.exemple.demo.entities.Utilisateur;
import com.exemple.demo.repositories.UtilisateurRepository;
import com.exemple.demo.service.UtilisateurService;

import jakarta.validation.Valid;
// import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class AuthController {

    private final UtilisateurService authService;
    // private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UtilisateurRepository utilisateurRepository;

    @PostMapping("/register")
    ResponseEntity<?> register(@Valid @RequestBody Utilisateur utilisateur) {
        try {
            return new ResponseEntity<>(authService.registerUser(utilisateur), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody Utilisateur utilisateur) throws Exception {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(utilisateur.getUsername(), utilisateur.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtUtils.generateToken(utilisateur.getUsername());
                Map<String, Object> body = new HashMap<>();
                // Utilisateur u=
                body.put("token", token);
                body.put("userId", utilisateurRepository.findUserByUsername(utilisateur.getUsername()).getId());
                body.put("role", utilisateurRepository.findUserByUsername(utilisateur.getUsername()).getRoles());
                return ResponseEntity.status(HttpStatus.OK).body(body);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Donnée incorrecte");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error internal Service");
        }
    }

    @GetMapping(path = "/check-token")
    public ResponseEntity<?> checkToken(@RequestParam("token") String token) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(jwtUtils.isTokenExpired(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error internal Service");
        }
    }

}
