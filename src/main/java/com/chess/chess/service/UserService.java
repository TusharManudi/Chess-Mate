package com.chess.chess.service;

import com.chess.chess.dto.RegisterDto;
import com.chess.chess.models.User;
import com.chess.chess.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder ;


    public String createUser(RegisterDto registerDto) {
        User user = new User() ;
        user.setEmail(registerDto.getEmail()) ;
        user.setPassword(passwordEncoder.encode(registerDto.getPassword())) ;
        user.setUsername(registerDto.getUsername()) ;
        userRepo.save(user) ;
        return user.getId().toString() ;
    }

}
