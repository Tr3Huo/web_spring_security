package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            RoleRepository roles,
            UserRepository users,
            PasswordEncoder encoder,
            @Value("${ADMIN_EMAIL:trungnh@hcmute.edu.vn}") String adminEmail,
            @Value("${ADMIN_PASSWORD:123456}") String adminPassword) {

        return args -> {
            Role userRole = roles.findByName("ROLE_USER").orElseGet(() -> roles.save(Role.builder().name("ROLE_USER").build()));
            Role adminRole = roles.findByName("ROLE_ADMIN").orElseGet(() -> roles.save(Role.builder().name("ROLE_ADMIN").build()));

            if (users.findByUsername("admin").isEmpty() && users.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail(adminEmail.toLowerCase());
                admin.setFullName("System Administrator");
                admin.setPassword(encoder.encode(adminPassword));
                admin.setRole(adminRole);
                admin.setEnabled(true);
                users.save(admin);
            }

            if (users.findByUsername("user01").isEmpty()) {
                User user = new User();
                user.setUsername("user01");
                user.setEmail("user01@gmail.com");
                user.setFullName("Nguyễn Hữu Trung");
                user.setPassword(encoder.encode("123456"));
                user.setRole(userRole);
                user.setEnabled(true);
                user.setImages("/images/user.png");
                users.save(user);
            }
        };
    }
}
