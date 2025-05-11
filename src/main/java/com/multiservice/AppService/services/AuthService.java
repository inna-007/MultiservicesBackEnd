package com.multiservice.AppService.services;



import com.multiservice.AppService.dto.ApiResponse;
import com.multiservice.AppService.dto.LoginRequest;
import com.multiservice.AppService.dto.RegisterRequest;
import com.multiservice.AppService.entities.*;
import com.multiservice.AppService.exceptions.auth.*;
import com.multiservice.AppService.exceptions.email.SendingEmailException;
import com.multiservice.AppService.repositories.RoleRepository;
import com.multiservice.AppService.repositories.UserRepository;
import com.multiservice.AppService.repositories.ClientRepository;
import com.multiservice.AppService.repositories.ProviderRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.multiservice.AppService.security.JwtUtils;

import java.util.*;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProviderRepository prestaRepository;


    @Transactional
    public ApiResponse<String> register(RegisterRequest request) throws Exception {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Un utilisateur avec cet email existe déjà.");
        }

        // Créer un nouvel utilisateur
        User user = new User();
        user.setName(request.getName());
        // If username is null or blank, use email as username
        user.setUsername((request.getUsername() == null || request.getUsername().isBlank()) ? request.getEmail() : request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(false);  // L'utilisateur doit activer son compte
        user.setActivationToken(UUID.randomUUID().toString());

        // Vérifier et assigner les rôles envoyés dans la requête
        Set<Role> userRoles = new HashSet<>();
        boolean isClient = false;
        boolean isPresta = false;

        // Always assign ROLE_CLIENT if no roles are provided
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (String roleName : request.getRoles()) {
                RoleName roleEnum = RoleName.valueOf(roleName.toUpperCase());
                Role role = roleRepository.findByName(roleEnum)
                        .orElseThrow(() -> new RuntimeException("Rôle non trouvé : " + roleName));
                userRoles.add(role);

                if (roleEnum == RoleName.ROLE_CLIENT) {
                    isClient = true;
                } else if (roleEnum == RoleName.ROLE_PROVIDER) {
                    isPresta = true;
                }
            }
        } else {
            Role defaultRole = roleRepository.findByName(RoleName.ROLE_CLIENT)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            userRoles.add(defaultRole);
            isClient = true;
        }

        user.setRoles(userRoles);

        // Sauvegarder l'utilisateur
        userRepository.save(user);

        // Ajouter à la table client ou presta si applicable
        if (isClient) {
            Client client = new Client();
            client.setUser(user);
            clientRepository.save(client);
        } else if (isPresta) {
            Provider presta = new Provider();
            presta.setUser(user);
            prestaRepository.save(presta);
        }

        // Envoyer l'email d'activation
        String activationLink = "http://localhost:8080/auth/activate?token=" + user.getActivationToken();
        Map<String, String> emailVariables = Map.of("activationLink", activationLink);
        String emailContent = emailService.loadEmailTemplate("templates/emails/activation-email.html", emailVariables);

        try {
            emailService.sendEmail(user.getEmail(), "Activation de votre compte", emailContent);
        } catch (MessagingException e) {
            throw new SendingEmailException("Erreur lors de l'envoi de l'email d'activation.");
        }

        return new ApiResponse<>("User registered successfully! Please check your email to activate your account.", HttpStatus.OK.value());
    }

    public String login(LoginRequest loginRequest) {
        String identifier = loginRequest.getIdentifier();  // Can be email or username
        Optional<User> user = userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByUsername(identifier));  // Check both email and username

        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Invalid email or username.");
        }

        // Authenticate the user with the correct username/email and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, loginRequest.getPassword())
        );

        // If authentication is successful, generate JWT token
        if (authentication.isAuthenticated()) {
            if (!user.get().isEnabled()) {
                throw new AccountIsNotEnabledException("Your account is not activated. Please check your email for the activation link.");
            }
            return jwtUtils.generateToken(authentication);  // Generate the JWT token
        } else {
            throw new InvalidCredentialsException("Invalid credentials.");
        }
    }

    public ApiResponse<String> activateAccount(String token) {
        Optional<User> userOptional = userRepository.findByActivationToken(token);

        if (userOptional.isEmpty()) {
            throw new InvalidTokenException("Token invalide !");
        }

        User user = userOptional.get();
        user.setEnabled(true);
        user.setActivationToken(null);
        userRepository.save(user);

        return new ApiResponse<>("User registered successfully! Please check your email to activate your account.", HttpStatus.OK.value());
    }

    public void forgotPassword(String email) throws Exception {
        // Check if the user exists with the provided email
        Optional<User> userOptional = userRepository.findByEmail(email);
        System.out.println(userOptional);
        // Throw a custom exception if the email is not found
        if (userOptional.isEmpty()) {
            throw new EmailNotFoundException("Email not found!");
        }

        User user = userOptional.get();
        String resetToken = UUID.randomUUID().toString();
        user.setActivationToken(resetToken);
        userRepository.save(user);

        // Générer le lien de réinitialisation
        String resetLink = "http://localhost:8080/auth/reset-password?token=" + resetToken;

        // Charger et personnaliser le modèle d'email
        Map<String, String> emailVariables = Map.of("resetLink", resetLink);
        String emailContent = emailService.loadEmailTemplate("templates/emails/reset-password-email.html", emailVariables);
        // Send the reset email
        emailService.sendEmail(user.getEmail(), "Réinitialisation du mot de passe", emailContent);
    }

    public void resetPassword(String token, String newPassword) {
        // Check if the user exists with the provided token
        Optional<User> userOptional = userRepository.findByActivationToken(token);

        // If token is invalid, throw a custom exception
        if (userOptional.isEmpty()) {
            throw new InvalidTokenException("Token invalide !");
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));  // Set new password
        user.setActivationToken(null);  // Remove token after use
        userRepository.save(user);
    }
}
