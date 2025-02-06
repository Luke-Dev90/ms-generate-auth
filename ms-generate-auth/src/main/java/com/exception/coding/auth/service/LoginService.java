package com.exception.coding.auth.service;

import com.exception.coding.auth.dto.AuthResponseDTO;
import com.exception.coding.auth.dto.UserLogged;
import com.exception.coding.auth.dto.UserLoginDTO;
import com.exception.coding.auth.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class LoginService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginService(AuthenticationManager authenticationManager,
                        UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<AuthResponseDTO> login(UserLoginDTO user) {


        if (!userService.isValidUser(user.getUsername(), user.getPassword())) {
            return ResponseEntity.status(401).body(new AuthResponseDTO("Credenciales invalidas"));
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );


        UserLogged userLogged = new UserLogged();

        userLogged.setUsername(user.getUsername());
        userLogged.setName(userService.getName(user.getUsername()));

        userLogged.setRoles(authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        String token = jwtUtil.generateJwt(userLogged);
        return ResponseEntity.ok(new AuthResponseDTO(token));

    }


}
