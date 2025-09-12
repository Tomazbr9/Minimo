package com.tomazbr9.minimo.controller;

import com.tomazbr9.minimo.dto.authDTO.JwtTokenDTO;
import com.tomazbr9.minimo.dto.authDTO.LoginDTO;
import com.tomazbr9.minimo.dto.userDTO.UserRequestDTO;
import com.tomazbr9.minimo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDTO request){
        service.registerUser(request);
        return new ResponseEntity<>("Usuário Criado com sucesso!", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDTO> authenticateUser(@RequestBody LoginDTO request){
        JwtTokenDTO token = service.authenticateUser(request);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}