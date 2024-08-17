package com.westpoint.dentalsys.login.service;


import com.westpoint.dentalsys.employee.Employee;
import com.westpoint.dentalsys.employee.EmployeeRepository;
import com.westpoint.dentalsys.login.model.AuthenticationResponse;
import com.westpoint.dentalsys.login.model.Role;
import com.westpoint.dentalsys.login.model.Token;
import com.westpoint.dentalsys.login.model.User;
import com.westpoint.dentalsys.login.repository.TokenRepository;
import com.westpoint.dentalsys.login.repository.UserRepository;
import com.westpoint.dentalsys.patient.PatientRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PatientRepository patientRepository;

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository,
                                 PatientRepository patientRepository, EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 TokenRepository tokenRepository,
                                 AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.patientRepository = patientRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request) {
        User user = new User();


        String role = String.valueOf(user.getRole());


        // check if user already exist. if exist than authenticate the user
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, null, "User already exist", role);
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (patientRepository.findByEmail(request.getUsername()).isPresent()) {
            user.setRole(Role.USER);

        } else if (employeeRepository.findByEmail(request.getUsername()).isPresent()) {

            Employee employee = employeeRepository.findByEmail(request.getUsername())
                    .orElseThrow(() -> new IllegalStateException((
                            "employee with email " + request.getUsername() + " does not exists")));

            user.setRole(employee.getRole());

        } else {
            user.setRole(Role.OTHER);
        }


        user = repository.save(user);

        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, refreshToken, "User registration was successful", role);

    }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        String role = String.valueOf(user.getRole());
        revokeAllTokenByUser(user);
        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, refreshToken, "User login was successful", role);

    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
        if (validTokens.isEmpty()) {
            return;
        }

        for (Token t : validTokens) {
            t.setLoggedOut(true);
        }

        tokenRepository.saveAll(validTokens);
    }

    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    public AuthenticationResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        AuthenticationResponse authResponse = null;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new AuthenticationResponse(null, null, "Error in header", null);
        }

        refreshToken = authHeader.substring(7);
        String username = jwtService.extractUsername(refreshToken);


        if (username != null) {

            var user = repository.findByUsername(username)
                    .orElseThrow();
            if (jwtService.isValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllTokenByUser(user);
                saveUserToken(accessToken, user);
                String role = String.valueOf(user.getRole());
                authResponse = new AuthenticationResponse(accessToken, refreshToken, "ok", role);

            }
        }

        return authResponse;
    }
}
