package com.chess.chess.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "app_user")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id ;

    @Column(nullable = false)
    private String username ;

    @Column(nullable = false,unique = true)
    private String email ;

    @Column(nullable = false)
    private String password ;

    private int rating = 500 ; //chess elo

    @OneToMany(mappedBy = "white")
    private List<Game> gamesAsWhite ;

    @OneToMany(mappedBy = "black")
    private List<Game> gamesAsBlack ;

    //Required by spring security

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email; // Spring Security requires this to be the unique identifier (email in our case)
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    // Method to get the display username (not the Spring Security username which is email)
    public String getDisplayName() {
        return username;
    }

}
