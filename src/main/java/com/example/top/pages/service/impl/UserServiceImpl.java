package com.example.top.pages.service.impl;

import com.example.top.pages.payload.response.*;
import com.example.top.pages.payload.request.JwtTokenRequest;
import com.example.top.pages.payload.request.RegistrationUser;
import com.example.top.pages.repository.RateRepository;
import com.example.top.pages.repository.RolesRepository;
import com.example.top.pages.repository.UserRepository;
import com.example.top.pages.models.User;
import com.example.top.pages.service.ActionHistoryService;
import com.example.top.pages.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final RateRepository rateRepository;
    private final ActionHistoryService actionHistoryService;

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
        if (registrationUser.getEmail().isEmpty()) {
            return new ResponseEntity<>(new AppResponse(HttpStatus.BAD_REQUEST.value(), "E-Mail пустой"), HttpStatus.BAD_REQUEST);
        }
        if (findByEmail(registrationUser.getEmail()).isPresent()) {
            return new ResponseEntity<>(new AppResponse(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким email уже создан"), HttpStatus.BAD_REQUEST);
        }
        User user = new User(
                registrationUser.getEmail(),
                passwordEncoder.encode(registrationUser.getPassword()),
                List.of(
                        rolesRepository.findByName("ROLE_USER")
                )
        );
        user.setRoles(List.of(rolesRepository.findByName("ROLE_USER")));
        if (registrationUser.getUsername() != null && !registrationUser.getUsername().isEmpty()) {
            Optional<User> userWithUsername = userRepository.findUserByName(registrationUser.getUsername());
            if (userWithUsername.isPresent()) {
                return new ResponseEntity<>(new AppResponse(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким username уже создан"), HttpStatus.BAD_REQUEST);
            }
            user.setName(registrationUser.getUsername());
        }
        createNewUser(user);
        actionHistoryService.createHistoryByType(
                ActionHistoryService.TypeHistory.USER_REGISTER,
                user,
                user.getId().toString()
                );
        return ResponseEntity.ok(new RegistrationResponse(HttpStatus.OK.value(),
                String.format("Пользователь %s зарегистрирован", registrationUser.getEmail())
        ));
    }

    public ResponseEntity<?> getUserInfo() {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        User userItem = userRepository.findCheckedUserByEmail(contextUser.getPrincipal().toString());
        Optional<UserInfoResponse> userInfo = rateRepository.getUserInfoWithARates(String.valueOf(userItem.getId()));

        if (userInfo.isPresent()) {
            return ResponseEntity.ok(userInfo.get());
        } else if (!userItem.getEmail().isEmpty()) {
            return ResponseEntity.ok(new UserInfoResponse(
                userItem.getId().toString(), userItem.getName(), userItem.getEmail(),
                    userItem.getDateOfBirth().toString(), userItem.getCreatedAt(), (long) 0
            ));
        }
        return new ResponseEntity<>(new AppResponse(HttpStatus.BAD_REQUEST.value(), "Пользователь не найден"), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> checkEmail(String email) {
        Optional<User> checkUser = userRepository.findUserByEmail(email);
        if (checkUser.isPresent()) return ResponseEntity.ok(new CheckEmailResponse(true));
        else return ResponseEntity.ok(new CheckEmailResponse(false));
    }
}
