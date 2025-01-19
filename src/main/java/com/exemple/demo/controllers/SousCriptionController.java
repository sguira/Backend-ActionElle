package com.exemple.demo.controllers;

import java.net.http.HttpHeaders;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemple.demo.config.JwtUtils;
import com.exemple.demo.entities.Assuree;
import com.exemple.demo.entities.Garante;
import com.exemple.demo.entities.ProduitAssure;
import com.exemple.demo.entities.Subscription;
import com.exemple.demo.entities.TypeClient;
import com.exemple.demo.entities.Utilisateur;
import com.exemple.demo.entities.Vehicule;
import com.exemple.demo.repositories.AssureeRepository;
import com.exemple.demo.repositories.ProduitAssureeRepository;
import com.exemple.demo.repositories.SubscriptionRepository;
import com.exemple.demo.repositories.UtilisateurRepository;
import com.exemple.demo.repositories.VehiculeRepository;
import com.exemple.demo.service.UserDetailsServiceCustom;
import com.exemple.demo.service.pdfMakerService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class SousCriptionController {

    private final SubscriptionRepository subscriptionRepository;
    private final AssureeRepository assureeRepository;
    private final VehiculeRepository vehiculeRepository;
    private final JwtUtils jwtUtils;
    private final UtilisateurRepository utilisateurRepository;
    private final UserDetailsServiceCustom userDetailsServiceCustom;
    private final ProduitAssureeRepository produitAssureeRepository;
    private final pdfMakerService pdfService;
    // private final AssureeRepository

    // génération du code de 12 caractères
    public String generateQuote() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    // méthode qui permet de faire une souscription
    @PostMapping("")
    @Transactional
    @Operation(description = "Cette méthode permet de faire une souscription à un produit d'assurance pour un client donné")
    public ResponseEntity<?> createSubscription(@RequestBody Subscription subscription,
            @RequestHeader("Authorization") String token) {

        try {
            if (token.startsWith("Bearer ")) {

                // recuperation de l'amazone qui veut faire la suscription à laide de son token
                token = token.substring(7);
                String username = jwtUtils.extractUsername(token);
                String amazoneId = utilisateurRepository.findUserByUsername(username).getId();

                // recupère les info concernant le vehicule puis l'enregistrer dans la table des
                // vehicule
                Vehicule vehicule = subscription.getVehicule();

                // Assuree assuree = subscription.getAssuree();
                subscription.setAmazoneId(amazoneId);
                subscription.setVehicule(vehiculeRepository.save(vehicule));
                subscription.setQuoteReference(generateQuote());
                // enregistrement de l'assuré
                subscription.setAssuree(assureeRepository.save(subscription.getAssuree()));

                // calcul des details du prime à payer

                ProduitAssure produit = produitAssureeRepository.findById(subscription.getProduitAssure().getId())
                        .get();
                System.out.print("Id Produit :" + produit.getId());
                Map<String, Object> detailsCalcul = calculerPrime(subscription, produit);
                System.out.print("Details:\n");
                System.out.println(detailsCalcul + "\n\n\n");

                subscription.setDetails(detailsCalcul);

                return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionRepository.save(subscription));
            } else {
                System.out.println("Non autorisé");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Invalid");
            }

        } catch (Exception e) {
            System.out.println("Erreur lors de la souscription");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // méthode pour calculer le prime à payer
    Map<String, Object> calculerPrime(Subscription subscription, ProduitAssure produitAssure) {

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> details = new HashMap<>();

        // pour le calcul de nombre d'année ecoulée depuis la mise en circulation
        LocalDate dateFin_ = new Date(System.currentTimeMillis()).toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
        LocalDate dateMiseCirculation = LocalDate.parse(subscription.getVehicule().getDateMiseCirculation());
        long nombreAnnee = ChronoUnit.YEARS.between(dateMiseCirculation, dateFin_);

        double primeTotal = 0;

        // parcours des garanties qui sont incluses dans le produit choisi
        for (Garante e : produitAssure.getGaranties()) {
            if (e.getName().equals("Garantie Responsabilité Civile")) {
                double rcPrime = subscription.getVehicule().getPuissance() == 2 ? 37601
                        : subscription.getVehicule().getPuissance() >= 3
                                && subscription.getVehicule().getPuissance() <= 6 ? 45181
                                        : subscription.getVehicule().getPuissance() >= 7
                                                && subscription.getVehicule().getPuissance() <= 10 ? 51078
                                                        : subscription.getVehicule().getPuissance() >= 11
                                                                && subscription.getVehicule().getPuissance() <= 14
                                                                        ? 65677
                                                                        : subscription.getVehicule()
                                                                                .getPuissance() >= 15
                                                                                && subscription.getVehicule()
                                                                                        .getPuissance() <= 23 ? 86456
                                                                                                : 104143;

                primeTotal += rcPrime;
                details.put("RC Prime", rcPrime);
            }
            if (e.getName().equals("GARANTIE DOMMAGES")) {
                double primeDommage = nombreAnnee < 5 ? subscription.getVehicule().getValeurVenale() * e.getRate() / 100
                        : 0;
                primeTotal += primeDommage;
                details.put("Prime Dommage", primeDommage);
            }
            if (e.getName().equals("GARANTIE TIERCE COLLISION") && e.getMaxAge() != 0
                    && e.getMaxAge() >= nombreAnnee) {
                double tierceColision = nombreAnnee <= 8
                        ? e.getRate() / 100 * subscription.getVehicule().getValeurVenale()
                        : 0;
                primeTotal += tierceColision;
                details.put("Prime Dommage", tierceColision);
            }
            if (e.getName().equals("GARANTIE TIERCE PLAFONNEE")) {
                if (e.getRate() * subscription.getVehicule().getPrixDachat() < 100000) {
                    if (nombreAnnee <= e.getMaxAge()) {
                        primeTotal += 100000;
                        details.put("Prime Tierce Plafonne", 100000);
                    }
                } else {
                    if (nombreAnnee <= e.getMaxAge()) {
                        double primePlafonnee = e.getRate() / 100 * subscription.getVehicule().getPrixDachat();
                        details.put("Prime Tierce Plafonne", primePlafonnee);
                    }
                }
            }
            if (e.getName().equals("GARANTIE VOL")) {
                double vol = e.getRate() / 100 * subscription.getVehicule().getValeurVenale();
                primeTotal += vol;
                details.put("Garantie Vol", vol);
            }
            if (e.getName().equals("GARANTIE INCENDIE")) {
                double garantieIncendie = e.getRate() / 100 * subscription.getVehicule().getValeurVenale();
                primeTotal += garantieIncendie;
                details.put("Garantie Incendie", garantieIncendie);
            }
        }
        result.put("Montant Prime", primeTotal);
        result.put("Details", details);

        return result;

    }

    // une méthode qui retourne l'identifiant d'utilsateur à partir du token
    String getuserIdByToken(String token) throws Exception {
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

    // retourne la liste de toutes les souscription par Utilisateur
    @GetMapping("")
    public ResponseEntity<?> getAllSubscriptionByUser(@RequestHeader("Authorization") String token) {
        // System.out.println("Appelé\n\n");
        try {
            String amazoneId = getuserIdByToken(token);
            return ResponseEntity.ok(subscriptionRepository.findByAmazoneId(amazoneId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // retourne une seule souscription à partir de son identifiant
    @GetMapping("/{id}")
    public ResponseEntity<?> getSingleSouscription(@PathVariable String id) {
        try {
            return ResponseEntity.ok(subscriptionRepository.findById(id).get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Géneration de l'attestation
    @GetMapping("/{id}/attestation")
    public ResponseEntity<byte[]> getAttestation(@PathVariable String id) {
        try {
            Subscription subscription = subscriptionRepository.findById(id).get();
            byte[] attestation = pdfService.generatePdfSouscription(subscription);
            return ResponseEntity.ok()
                    .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=rapport.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(attestation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // changer le status d'un assuré
    @PatchMapping("/statusAssuree/{id}")
    public ResponseEntity<?> changeStatusSouscription(@PathVariable String id) {

        try {
            Assuree assuree = assureeRepository.findById(id).get();
            assuree.setType(TypeClient.CLIENT);// change le status de l'assuré
            return ResponseEntity.ok(assureeRepository.save(assuree));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    // liste des souscriptions pour un client donnée
    @GetMapping("/clients/{id}")
    ResponseEntity<Object> getSouscriptionByCustom(@PathVariable String id) {
        try {
            List<Subscription> subscriptions = new ArrayList<>();

            for (Subscription s : subscriptionRepository.findAll()) {
                if (s.getAssuree().getId().equals(id)) {
                    subscriptions.add(s);
                }
            }
            return ResponseEntity.ok(subscriptions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne du server");
        }
    }

}
