package com.example.top.pages.service.impl;

import com.example.top.pages.repository.RolesRepository;
import com.example.top.pages.repository.UserRepository;
import com.example.top.pages.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        Optional<User> tempUser = userRepository.findUserByUsername(username);
        if (tempUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        } else {
            return tempUser.get();
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with username %s not found", username)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(roles -> new SimpleGrantedAuthority(roles.getName())).collect(Collectors.toList())
        );
    }

    public void createNewUser(User user) {
        user.setRoles(List.of(rolesRepository.findByName("ROLE_USER")));
        userRepository.save(user);
    }
}
