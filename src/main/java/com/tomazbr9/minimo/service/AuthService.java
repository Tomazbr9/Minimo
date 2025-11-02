package com.tomazbr9.minimo.service;

import com.tomazbr9.minimo.MinimoApplication;
import com.tomazbr9.minimo.dto.authDTO.JwtTokenDTO;
import com.tomazbr9.minimo.dto.authDTO.LoginDTO;
import com.tomazbr9.minimo.dto.userDTO.UserRequestDTO;
import com.tomazbr9.minimo.model.Role;
import com.tomazbr9.minimo.model.User;
import com.tomazbr9.minimo.repository.RoleRepository;
import com.tomazbr9.minimo.repository.UserRepository;
import com.tomazbr9.minimo.security.SecurityConfiguration;
import com.tomazbr9.minimo.security.jwt.JwtTokenService;
import com.tomazbr9.minimo.security.model.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    public void registerUser(UserRequestDTO request){

        Role role = roleRepository.findByName(request.role()).orElseThrow(() -> new RuntimeException("Papel não encontrado"));

        User user = User.builder()
                .username(request.username())
                .password(securityConfiguration.passwordEncoder().encode(request.password()))
                .roles(List.of(role))
                .build();


        userRepository.save(user);

        logger.info("Usuário salvo no banco de dados");
    }

    public JwtTokenDTO authenticateUser(LoginDTO request){

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new JwtTokenDTO(jwtTokenService.generateToken(userDetails));
    }

}
