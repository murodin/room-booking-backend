package muro.room.booking.controller;

import muro.room.booking.entity.User;
import muro.room.booking.model.AngularUser;
import muro.room.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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


}