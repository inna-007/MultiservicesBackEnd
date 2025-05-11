package com.multiservice.AppService;

import com.multiservice.AppService.entities.Role;
import com.multiservice.AppService.entities.RoleName;
import com.multiservice.AppService.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

	@Bean
	public CommandLineRunner loadRoles(RoleRepository roleRepository) {
		return args -> {
			for (RoleName roleName : RoleName.values()) {
				roleRepository.findByName(roleName).orElseGet(() -> {
					return roleRepository.save(new Role(null, roleName));
				});
			}
		};
	}
}
