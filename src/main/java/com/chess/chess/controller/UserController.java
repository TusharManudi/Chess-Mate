package com.chess.chess.controller;

import com.chess.chess.dto.LoginDto;
import com.chess.chess.dto.LoginResponseDto;
import com.chess.chess.dto.RegisterDto;
import com.chess.chess.models.User;
import com.chess.chess.repo.UserRepo;
import com.chess.chess.service.JWTService;
import com.chess.chess.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTService  jwtService;
    private final UserRepo userRepo;

    @PostMapping("/auth/register")
    public ResponseEntity<?> createUser(@RequestBody RegisterDto registerDto){
        String id = userService.createUser(registerDto) ;
        return new ResponseEntity<>(id , HttpStatus.CREATED) ;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        //authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtService.generateToken((UserDetails) authentication.getPrincipal());
        Optional<User> op = userRepo.findByEmail(loginDto.getEmail());
        User user = null;
        if (op.isPresent()) {
            user = op.get();
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password incorrect");
        }
        return ResponseEntity.ok(new LoginResponseDto(jwtToken, user.getDisplayName(), user.getId().toString()));
    }




}
