package com.mams.service;

import com.mams.dto.*;
import com.mams.entity.User;
import com.mams.repository.UserRepository;
import com.mams.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager manager;
    private final UserRepository users;
    private final JwtService jwt;
    private final PasswordEncoder encoder;

    public AuthService(AuthenticationManager manager,UserRepository users,JwtService jwt,PasswordEncoder encoder){
        this.manager=manager;this.users=users;this.jwt=jwt;this.encoder=encoder;
    }
    public LoginResponse login(LoginRequest req){
        manager.authenticate(new UsernamePasswordAuthenticationToken(req.username(),req.password()));
        User u=users.findByUsername(req.username()).orElseThrow();
        UserDetails details=org.springframework.security.core.userdetails.User.withUsername(u.getUsername())
                .password(u.getPassword()).roles(u.getRole().name()).build();
        return new LoginResponse(jwt.generateToken(details),u.getUsername(),u.getFullName(),u.getRole().name(),
                u.getBase()==null?null:u.getBase().getId(),u.getBase()==null?null:u.getBase().getName());
    }
}
