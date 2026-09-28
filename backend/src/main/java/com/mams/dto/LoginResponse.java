package com.mams.dto;
public record LoginResponse(String token, String username, String fullName, String role, Long baseId, String baseName) {}
