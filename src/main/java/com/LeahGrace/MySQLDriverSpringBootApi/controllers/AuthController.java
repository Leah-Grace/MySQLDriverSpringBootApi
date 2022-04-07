package com.LeahGrace.MySQLDriverSpringBootApi.controllers;

import com.LeahGrace.MySQLDriverSpringBootApi.models.auth.ERole;
import com.LeahGrace.MySQLDriverSpringBootApi.models.auth.Role;
import com.LeahGrace.MySQLDriverSpringBootApi.models.auth.User;
import com.LeahGrace.MySQLDriverSpringBootApi.payloads.request.LoginRequest;
import com.LeahGrace.MySQLDriverSpringBootApi.payloads.request.SignupRequest;
import com.LeahGrace.MySQLDriverSpringBootApi.payloads.response.JwtResponse;
import com.LeahGrace.MySQLDriverSpringBootApi.payloads.response.MessageResponse;
import com.LeahGrace.MySQLDriverSpringBootApi.repositories.RoleRepository;
import com.LeahGrace.MySQLDriverSpringBootApi.repositories.UserRepository;
import com.LeahGrace.MySQLDriverSpringBootApi.security.jwt.JwtUtils;
import com.LeahGrace.MySQLDriverSpringBootApi.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin)")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        //this will undo the conversion and put back into List<String> format

        return ResponseEntity.ok(new JwtResponse(
                                                    jwt,
                                                    userDetails.getId(),
                                                    userDetails.getUsername(),
                                                    roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?>  registerUser(@RequestBody SignupRequest signupRequest) {
        //check if user exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email already in use, please login or reset password"));
        }

        //create new account

        User user = new User(signupRequest.getUsername(), encoder.encode(signupRequest.getPassword())); //encoder is in websecurityconfig

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch(role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(userRole);

                        break;


                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity(new MessageResponse("User registered successfully"), HttpStatus.CREATED);
    }

}
