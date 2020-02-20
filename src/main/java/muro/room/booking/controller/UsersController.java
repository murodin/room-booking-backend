package muro.room.booking.controller;

import muro.room.booking.entity.User;
import muro.room.booking.model.AngularUser;
import muro.room.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public List<AngularUser> getAllUsers(){
        return userRepository.findAll().parallelStream().map( user -> new AngularUser(user)).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AngularUser getUser(@PathVariable("id") Long id) {
        return new AngularUser(userRepository.findById(id).get());
    }

    @PutMapping()
    public AngularUser updateUser(@RequestBody AngularUser updatedUser) throws InterruptedException {
        User originalUser = userRepository.findById(updatedUser.getId()).get();
        originalUser.setName(updatedUser.getName());
        return new AngularUser(userRepository.save(originalUser));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
    }

    @PostMapping()
    public AngularUser newUser(@RequestBody User user) {
        return new AngularUser(userRepository.save(user));
    }

    @GetMapping("/resetPassword/{id}")
    public void resetPassword(@PathVariable("id") Long id){
        User user = userRepository.findById(id).get();
        user.setPassword("secret");
        userRepository.save(user);
    }

    @GetMapping("/currentUserRole")
    public Map<String, String> getCurrentUserRole() {
        Collection<GrantedAuthority> roles = (Collection<GrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        String role = roles.stream().findAny().orElse(null).getAuthority().substring(5);
        Map<String,String> result = new HashMap<>();
        result.put("role", role);
        return result;
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/api");
        cookie.setHttpOnly(true);
        //todo: when the prod should be true cookie.setSecure(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        SecurityContextHolder.getContext().setAuthentication(null);
        return "";
    }

}