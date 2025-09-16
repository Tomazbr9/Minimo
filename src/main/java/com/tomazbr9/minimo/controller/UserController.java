package com.tomazbr9.minimo.controller;

import com.tomazbr9.minimo.dto.userDTO.UserResponseDTO;
import com.tomazbr9.minimo.dto.userDTO.UserUpdateDTO;
import com.tomazbr9.minimo.model.User;
import com.tomazbr9.minimo.security.model.UserDetailsImpl;
import com.tomazbr9.minimo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<UserResponseDTO> findAuthenticatedUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        UserResponseDTO response = service.findAuthenticatedUser(userDetails);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserUpdateDTO dto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        UserResponseDTO response = service.updateUser(dto, userDetails);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        service.deleteUser(userDetails);
        return ResponseEntity.noContent().build();
    }
}
