package com.example.uberprojectauthservice.controllers;

import com.example.uberprojectauthservice.dto.*;
import com.example.uberprojectauthservice.helper.AuthDriverDetails;
import com.example.uberprojectauthservice.helper.AuthPassengerDetails;
import com.example.uberprojectauthservice.services.AuthService;
import com.example.uberprojectauthservice.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Value("${cookie.expiry}")
    private int cookieExpiry;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup/passenger")
    public ResponseEntity<PassengerDto>singUp(@RequestBody PassengerSignupRequestDto passengerSignupRequestDto){
        PassengerDto response = authService.signupPassenger(passengerSignupRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
//    @PostMapping("/signin/passenger")
//    public ResponseEntity<?>signIn(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse response){
//        Authentication authentication =authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(),authRequestDto.getPassword()));
//        if (!(authentication.getPrincipal() instanceof AuthPassengerDetails)) {
//            throw new UsernameNotFoundException("No passenger found with this email");
//        }
//        System.out.println(authentication);
//        if(authentication.isAuthenticated()){
////          Map<String,Object>payload=new HashMap<>();
////          payload.put("email",authRequestDto.getEmail());
////          String jwtToken=jwtService.createToken(payload, Objects.requireNonNull(authentication.getPrincipal()).toString());
//            String jwtToken=jwtService.createToken(authRequestDto.getEmail());
//            System.out.println("Generated Token: " + jwtToken); // Add this line to log the token
//            ResponseCookie cookie= ResponseCookie.from("JwtToken",jwtToken)
//                                    .httpOnly(true)
//                                    .secure(false)//set to true in production
//                                    .path("/")
//                                    .maxAge(cookieExpiry)
//                                    .build();
//            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//            System.out.println(authRequestDto.getEmail());
//            return new ResponseEntity<>(jwtToken,HttpStatus.OK);
//        }else {
//            //return new ResponseEntity<>("Auth not Successful", HttpStatus.OK);
//            throw new UsernameNotFoundException("user not fount");
//        }
//    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return new ResponseEntity<>("No cookies found", HttpStatus.UNAUTHORIZED);
        }

        String jwtToken = null;
        for (Cookie cookie : cookies) {
            if ("JwtToken".equals(cookie.getName())) {
            jwtToken = cookie.getValue();
             }
         }

        if (jwtToken == null) {
        return new ResponseEntity<>("No JwtToken cookie", HttpStatus.UNAUTHORIZED);
        }

    return new ResponseEntity<>("Success", HttpStatus.OK);
}

    @PostMapping("/signup/driver")
    public ResponseEntity<DriverDto>signUp(@RequestBody DriverSignupRequestDto driverSignupRequestDto){
        DriverDto response = authService.signupDriver(driverSignupRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));

        if (!authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("user not found");
        }

        Object principal = authentication.getPrincipal();
        String role;
        Long id;

        if (principal instanceof AuthPassengerDetails passengerDetails) {
            role = "PASSENGER";
            id=passengerDetails.getId();
        } else if (principal instanceof AuthDriverDetails driverDetails) {
            role = "DRIVER";
            id=driverDetails.getId();
        } else {
            // shouldn't happen, but fail safely if some other principal type shows up
            throw new UsernameNotFoundException("Unknown account type");
        }

        String jwtToken = jwtService.createToken(authRequestDto.getEmail());

        ResponseCookie cookie = ResponseCookie.from("JwtToken", jwtToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(cookieExpiry)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<>(new AuthResponseDto(id,true,role), HttpStatus.OK);
    }

}
