package com.example.DroneApp.Service;

import com.example.DroneApp.Exception.InformationExistException;
import com.example.DroneApp.Model.User;
import com.example.DroneApp.Repository.UserRepository;
import com.example.DroneApp.Security.JWTUtils;
import com.example.DroneApp.Security.MyUserDetails;
import com.example.DroneApp.Dto.LoginRequestDto;
import com.example.DroneApp.Dto.LoginResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private MyUserDetails myUserDetails;

    @Autowired
    public UserService(UserRepository userRepository,
                       @Lazy PasswordEncoder passwordEncoder,
                       JWTUtils jwtUtils,
                       @Lazy AuthenticationManager authenticationManager,
                       @Lazy MyUserDetails myUserDetails) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.myUserDetails = myUserDetails;
    }

    public User createUser(User userObject) {
        logger.info("Service Calling createUser ==> ");
        if (!userRepository.existsByEmailAddress(userObject.getEmailAddress())) {
            userObject.setPassword(passwordEncoder.encode(userObject.getPassword()));
            return userRepository.save(userObject);
        } else {
            logger.warn("Attempt to register existing user with email: {}", userObject.getEmailAddress());
            throw new InformationExistException("User with email address " + userObject.getEmailAddress() + " already exists.");
        }
    }

    public User findUserByEmailAddress(String email) {
        logger.debug("Finding user by email: {}", email);
        return userRepository.findUserByEmailAddress(email);
    }

    public ResponseEntity<?> loginUser(LoginRequestDto loginRequestDto) {
        logger.info("Service Calling loginUser ==> for email: {}", loginRequestDto.getEmail());
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            myUserDetails = (MyUserDetails) authentication.getPrincipal();
            final String JWT = jwtUtils.generateJwtToken(myUserDetails);

            logger.info("Login successful for user: {}", loginRequestDto.getEmail());
            return ResponseEntity.ok(new LoginResponseDto(JWT));
        } catch (Exception e) {
            logger.error("Login failed for user: {} - {}", loginRequestDto.getEmail(), e.getMessage());
            return ResponseEntity.ok(new LoginResponseDto("Error : username or password is incorrect"));
        }
    }
}