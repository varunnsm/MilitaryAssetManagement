package com.mams.controller;
import com.mams.dto.*; import com.mams.service.AuthService;
import jakarta.validation.Valid; import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth){this.auth=auth;}
    @PostMapping("/login") public LoginResponse login(@RequestBody LoginRequest req){return auth.login(req);}
}
