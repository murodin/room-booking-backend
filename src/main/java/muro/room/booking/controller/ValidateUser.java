package muro.room.booking.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/basicAuth")
public class ValidateUser {

    @GetMapping("/validate")
    public String userIsValid() {
        return "{\"result\":\"ok\"}";
    }

}
