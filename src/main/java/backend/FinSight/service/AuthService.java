package backend.FinSight.service;

import backend.FinSight.dto.LoginRequest;
import backend.FinSight.dto.RegisterRequest;
import backend.FinSight.model.User;
import backend.FinSight.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // REGISTER

    public User register(RegisterRequest request) {

        // EMAIL EXISTS

        if (userRepository.findByEmail(
                request.getEmail()).isPresent()) {

            throw new RuntimeException(
                    "Email already exists");
        }

        // USERNAME EXISTS

        if (userRepository.findByUsername(
                request.getUsername()).isPresent()) {

            throw new RuntimeException(
                    "Username already exists");
        }

        User user = new User();

        user.setName(request.getName());

        user.setUsername(request.getUsername());

        user.setEmail(request.getEmail());

        // ENCRYPT PASSWORD

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        // DEFAULT ROLE

        user.setRole("USER");

        return userRepository.save(user);
    }

    // LOGIN

    public User login(LoginRequest request) {

        User user = userRepository
                .findByEmailOrUsername(
                        request.getEmailOrUsername(),
                        request.getEmailOrUsername()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        // PASSWORD CHECK

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {

            throw new RuntimeException(
                    "Invalid password"
            );
        }

        // ROLE CHECK

        if (request.getRole() == null ||

                !user.getRole()
                        .equalsIgnoreCase(
                                request.getRole()
                        )) {

            throw new RuntimeException(
                    "Invalid role"
            );
        }

        return user;
    }
}