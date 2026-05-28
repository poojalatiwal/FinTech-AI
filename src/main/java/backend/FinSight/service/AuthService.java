package backend.FinSight.service;

import backend.FinSight.dto.LoginRequest;
import backend.FinSight.dto.RegisterRequest;
import backend.FinSight.model.User;
import backend.FinSight.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User register(RegisterRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
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
                        new RuntimeException("User not found"));

        if (!user.getPassword()
                .equals(request.getPassword())) {

            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}
