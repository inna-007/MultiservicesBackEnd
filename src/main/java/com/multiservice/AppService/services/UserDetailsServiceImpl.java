package com.multiservice.AppService.services;


import com.multiservice.AppService.entities.User;
import com.multiservice.AppService.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
// Annotation indiquant que cette classe est un service Spring, utilisée pour la logique métier
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Dépendance vers le repository de l'utilisateur, injectée via le constructeur
    private final UserRepository userRepository;

    // Constructeur pour l'injection des dépendances (UserRepository)
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;  // L'instance de repository sera utilisée pour récupérer les données utilisateurs
    }

    // === Chargement de l'utilisateur par email pour l'authentification ===
    // Cette méthode est appelée par Spring Security lors de l'authentification pour charger un utilisateur à partir de son email.
    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        // Essayer de trouver par email
        User user = userRepository.findByEmail(input)
                // Sinon essayer par username
                .orElseGet(() -> userRepository.findByUsername(input)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email or username: " + input)));

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // Tu peux aussi mettre user.getUsername() ici si tu préfères
                user.getPassword(),
                authorities
        );
    }}


