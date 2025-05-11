package com.multiservice.AppService.services;

import com.multiservice.AppService.dto.ClientProfileDTO;
import com.multiservice.AppService.dto.ProviderProfileDTO;
import com.multiservice.AppService.entities.Client;
import com.multiservice.AppService.entities.Provider;
import com.multiservice.AppService.entities.User;
import com.multiservice.AppService.repositories.ClientRepository;
import com.multiservice.AppService.repositories.ProviderRepository;
import com.multiservice.AppService.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ProviderRepository providerRepository;

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
        // log instead of throwing exception after deletion
        System.out.println("User successfully deleted!");
    }

    @Transactional
    public void updateClientProfile(String email, ClientProfileDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Client client = clientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Client profile not found"));

        client.setAddress(dto.getAddress());
        client.setTel(dto.getTel());
        clientRepository.save(client);

        System.out.println("Updating client profile for " + email);
        System.out.println("New Address: " + dto.getAddress());
        System.out.println("New Tel: " + dto.getTel());
        System.out.println("Client saved with ID: " + client.getId());
    }

    @Transactional
    public void updateProviderProfile(String email, ProviderProfileDTO dto) {
        Provider provider = providerRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Provider profile not found"));

        provider.setCity(dto.getCity());
        provider.setPhoneNumber(dto.getPhoneNumber());
        provider.setCin(dto.getCin());
        provider.setDisponibilites(dto.getDisponibilites());
        providerRepository.save(provider);

        System.out.println("Updated provider profile for " + email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
