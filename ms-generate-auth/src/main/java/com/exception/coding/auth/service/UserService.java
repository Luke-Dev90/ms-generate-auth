package com.exception.coding.auth.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService  implements UserDetailsService {

    private final Map<String,String> users = new HashMap<>();
    private final Map<String, String> roles = new HashMap<>();
    private final Map<String, String> names = new HashMap<>();
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        users.put("admin",passwordEncoder.encode("1234pass"));
        roles.put("admin", "ADMIN");
        names.put("admin","Lucas");

        users.put("luke",passwordEncoder.encode("1234user"));
        roles.put("luke",  "USER");
        names.put("luke","Lucas");
    }

    public boolean isValidUser(String username, String password) {
        if (!users.containsKey(username)) {
            return false;
        }
        String storedPassword = users.get(username);
        return passwordEncoder.matches(password, storedPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!users.containsKey(username)) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        return User.builder()
                .username(username)
                .password(users.get(username))
                .roles(roles.get(username))
                .build();
    }

    public String getName(String username){
        return names.get(username);
    }

}
