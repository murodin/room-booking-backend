package muro.room.booking.controller;


import muro.room.booking.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/basicAuth")
public class ValidateUser {

    @Autowired
    private JWTService jwtService;

    @GetMapping("/validate")
    public Map<String, String> userIsValid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        String name = userDetails.getUsername();
        String role = userDetails.getAuthorities().toArray()[0].toString().substring(5);

        String token = jwtService.generateToken(name, role);

        Map<String, String> result = new HashMap<>();
        result.put("result", token);

        return result;
    }

}
