package com.example.top.pages.service.impl;

import com.example.top.pages.exceptions.AppException;
import com.example.top.pages.payload.request.JwtTokenRequest;
import com.example.top.pages.payload.request.RegistrationUser;
import com.example.top.pages.payload.response.JwtResponse;
import com.example.top.pages.payload.response.RegistrationResponse;
import com.example.top.pages.repository.RolesRepository;
import com.example.top.pages.repository.UserRepository;
import com.example.top.pages.models.User;
import com.example.top.pages.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;

    public List<User> getUserList() {return userRepository.findAll();}
    public Optional<User> findByEmail(String email) {return userRepository.findUserByEmail(email);}
    public void createNewUser(User user) {userRepository.save(user);}

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with email %s not found", email)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(roles -> new SimpleGrantedAuthority(roles.getName())).collect(Collectors.toList())
        );
    }


    public ResponseEntity<?> authorization(JwtTokenRequest jwtTokenRequest) {
        UserDetails userDetails = loadUserByUsername(jwtTokenRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> registration(RegistrationUser registrationUser) {
        if (findByEmail(registrationUser.getEmail()).isPresent()) {
            return new ResponseEntity<>(new AppException(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким email уже создан"), HttpStatus.BAD_REQUEST);
        }
        User user = new User(
                registrationUser.getEmail(),
                passwordEncoder.encode(registrationUser.getPassword()),
                List.of(
                        rolesRepository.findByName("ROLE_USER")
                )
        );
        user.setRoles(List.of(rolesRepository.findByName("ROLE_USER")));
        createNewUser(user);
        return ResponseEntity.ok(new RegistrationResponse(HttpStatus.OK.value(),
                String.format("Пользователь %s зарегистрирован", registrationUser.getEmail())
        ));
    }
}
