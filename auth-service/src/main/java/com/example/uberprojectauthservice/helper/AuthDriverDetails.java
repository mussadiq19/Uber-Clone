package com.example.uberprojectauthservice.helper;

import com.example.uberprojectentityservice.models.Driver;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AuthDriverDetails extends Driver implements UserDetails {
    private final String username ;
    private final String password;
    private final Driver driver;
    public AuthDriverDetails(Driver driver) {
        this.driver = driver;
        this.username = driver.getEmail();
        this.password = driver.getPassword();
    }
    public Long getId(){
        return driver.getId();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
