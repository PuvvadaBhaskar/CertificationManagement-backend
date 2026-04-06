package com.project.service;

import java.util.Map;

public interface AuthService {

    Map<String, Object> login(Map<String, String> request);

    String refresh(String refreshToken);

    void logout(String refreshToken);

    String register(com.project.model.User user);
}