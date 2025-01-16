package com.exemple.demo.controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemple.demo.config.JwtUtils;
import com.exemple.demo.entities.Garante;
import com.exemple.demo.entities.ProduitAssure;
import com.exemple.demo.entities.Simulation;
import com.exemple.demo.repositories.ProduitAssureeRepository;
import com.exemple.demo.repositories.SimulationRepository;
import com.exemple.demo.repositories.UtilisateurRepository;
import com.exemple.demo.service.SimulationService;
import com.exemple.demo.service.UserDetailsServiceCustom;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SimulationController {

    private final SimulationRepository simulationRepository;
    // private final ProduitAssureeRepository produitAssureRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final UserDetailsServiceCustom userDetailsServiceCustom;
    private final JwtUtils jwtUtils;
    private final SimulationService simulationService;

    @PostMapping("/simulations")
    // @Operation(summary = "Cette méthode permet de faire une simulation de dévis")
    // @ApiResponse(responseCode = "200", description = "retourne la simulation du
    // dévis qui contient des informations comme le montant total de prime")
    ResponseEntity<?> createSimulation(@RequestBody Simulation simulation, @RequestHeader("Authorization") String token)
            throws Exception {
        try {
            // System.out.println(simulate.getProduitAssure().getNomProduit());
            System.out.println("Méthode appeléé valeur venale" + simulation.getValeurVenale() + " \n\n\n");

            String userId = getuserIdByToken(token);
            simulation.setUserId(userId);
            double primeTotal = 0;

            // pour le calcul de nombre d'année ecoulée depuis la mise en circulation
            LocalDate dateFin_ = new Date(System.currentTimeMillis()).toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate dateMiseCirculation = LocalDate.parse(simulation.getMiseCirculation());
            long nombreAnnee = ChronoUnit.YEARS.between(dateMiseCirculation, dateFin_);

            Map<String, Object> result = new HashMap<>();
            Map<String, Object> details = new HashMap<>();

            simulation.setCreatedAt((new Date(System.currentTimeMillis())).toString());
            for (Garante e : simulation.getProduitAssure().getGaranties()) {
                if (e.getName().equals("Garantie Responsabilité Civile")) {
                    double rcPrime = simulation.getPuissance() == 2 ? 37601
                            : simulation.getPuissance() >= 3 && simulation.getPuissance() <= 6 ? 45181
                                    : simulation.getPuissance() >= 7 && simulation.getPuissance() <= 10 ? 51078
                                            : simulation.getPuissance() >= 11
                                                    && simulation.getPuissance() <= 14
                                                            ? 65677
                                                            : simulation.getPuissance() >= 15
                                                                    && simulation.getPuissance() <= 23 ? 86456
                                                                            : 104143;

                    primeTotal += rcPrime;
                    details.put("RC Prime", rcPrime);
                }
                if (e.getName().equals("GARANTIE DOMMAGES")) {
                    double primeDommage = nombreAnnee < 5 ? simulation.getValeurVenale() * e.getRate() / 100 : 0;
                    primeTotal += primeDommage;
                    details.put("Prime Dommage", primeDommage);
                }
                if (e.getName().equals("GARANTIE TIERCE COLLISION") && e.getMaxAge() != 0
                        && e.getMaxAge() >= nombreAnnee) {
                    double tierceColision = nombreAnnee <= 8 ? e.getRate() / 100 * simulation.getValeurVenale() : 0;
                    primeTotal += tierceColision;
                    details.put("Prime Dommage", tierceColision);
                }
                if (e.getName().equals("GARANTIE TIERCE PLAFONNEE")) {
                    if (e.getRate() * simulation.getValeurNeuf() < 100000) {
                        if (nombreAnnee <= e.getMaxAge()) {
                            primeTotal += 100000;
                            details.put("Prime Tierce Plafonne", 100000);
                        }
                    } else {
                        if (nombreAnnee <= e.getMaxAge()) {
                            double primePlafonnee = e.getRate() / 100 * simulation.getValeurNeuf();
                            details.put("Prime Tierce Plafonne", primePlafonnee);
                        }
                    }
                }
                if (e.getName().equals("GARANTIE VOL")) {
                    double vol = e.getRate() / 100 * simulation.getValeurVenale();
                    primeTotal += vol;
                    details.put("Garantie Vol", vol);
                }
                if (e.getName().equals("GARANTIE INCENDIE")) {
                    double garantieIncendie = e.getRate() / 100 * simulation.getValeurVenale();
                    primeTotal += garantieIncendie;
                    details.put("Garantie Incendie", garantieIncendie);
                }
            }

            // on ajoute 1209600000 qui est la valeur de 2 semaines en milisecondes à la
            // valeur de la date pour obtenir la date de fin du dévis

            String dateFin = new Date(System.currentTimeMillis() + 1209600000).toString();
            result.put("Date dévis", (new Date(System.currentTimeMillis())).toString());
            result.put("Prime Total", primeTotal);
            result.put("Date fin du dévi", dateFin);
            result.put("Nom du produit", simulation.getProduitAssure().getNomProduit());
            result.put("Puissance Cv", simulation.getPuissance());
            result.put("Valeur Venale", simulation.getValeurVenale());
            result.put("Détails du Calcul du prime", details);
            result.put("quoteReference", "QT" + simulationService.generateQuote());
            simulation.setDetails(result);
            simulationRepository.save(simulation);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/simulations")
    ResponseEntity allSumilation(@RequestHeader("Authorization") String token) {
        try {
            String userId = getuserIdByToken(token);
            return ResponseEntity.ok(simulationRepository.findByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // une méthode qui retourne le nom d'utilsateur à partir du token
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

    // retourne une simulation à laide de son identifiant
    @GetMapping("/simulations/{id}")
    ResponseEntity getUnique(@PathVariable String id) {
        try {
            return ResponseEntity.ok(simulationRepository.findById(id).get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
