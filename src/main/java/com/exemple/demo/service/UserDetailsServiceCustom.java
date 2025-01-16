package com.exemple.demo.service;

import java.util.Arrays;
import java.util.Collection;

import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.exemple.demo.entities.Utilisateur;
import com.exemple.demo.repositories.UtilisateurRepository;

import lombok.RequiredArgsConstructor;

@Component
@Service
@RequiredArgsConstructor
public class UserDetailsServiceCustom implements UserDetailsService {

    final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findUserByUsername(username);
        if (utilisateur == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé");
        }

        return new org.springframework.security.core.userdetails.User(utilisateur.getUsername(),
                utilisateur.getPassword(), mapRolesToAuthorities(utilisateur.getRoles()));

    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(String roles) {
        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
