package com.example.uberprojectauthservice.helper;

import com.example.uberprojectentityservice.models.Passenger;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
//why we need this class
//because spring security works on User details polymorphic type of auth
public class AuthPassengerDetails extends Passenger implements UserDetails {
    private final Passenger passenger;
    private final String username;
    private final String password;
    public AuthPassengerDetails(Passenger passenger){
        this.passenger=passenger;
        this.username=passenger.getEmail();
        this.password=passenger.getPassword();
    }
    public Long getId(){
        return passenger.getId();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of();
    }
    @Override
    public String getUsername() {
        return this.username;
    }
    @Override
    public @Nullable String getPassword()  {
        return this.password; 
    }

}
