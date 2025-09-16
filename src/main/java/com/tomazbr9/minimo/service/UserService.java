package com.tomazbr9.minimo.service;

import com.tomazbr9.minimo.dto.userDTO.UserResponseDTO;
import com.tomazbr9.minimo.dto.userDTO.UserUpdateDTO;
import com.tomazbr9.minimo.model.User;
import com.tomazbr9.minimo.repository.UserRepository;
import com.tomazbr9.minimo.security.model.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO findAuthenticatedUser(UserDetailsImpl userDetails){
        User user = checkIfUserExists(userDetails);
        return new UserResponseDTO(user.getId(), user.getUsername());
    }

    public UserResponseDTO updateUser(UserUpdateDTO dto, UserDetailsImpl userDetails){

        User user = checkIfUserExists(userDetails);

        user.setUsername(dto.username());
        user.setPassword(dto.password());

        userRepository.save(user);
        return new UserResponseDTO(user.getId(), user.getUsername());
    }

    public void deleteUser(UserDetailsImpl userDetails){
        User user = checkIfUserExists(userDetails);
        userRepository.delete(user);
    }

    private User checkIfUserExists(UserDetailsImpl userDetails){
        return userRepository.findByUsername(
                userDetails.getUsername()
        ).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }

}
