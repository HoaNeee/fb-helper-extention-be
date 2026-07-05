package com.hoane.fbhelper.fbhelperextentionbe.config;

import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User u = User.builder()
                .username("admin")
                .email("admin@gmail.com")
                .build();

        String password = "admin";
        String hash = passwordEncoder.encode(password);
        u.setPassword(hash);

        userRepository.save(u);
    }
}
