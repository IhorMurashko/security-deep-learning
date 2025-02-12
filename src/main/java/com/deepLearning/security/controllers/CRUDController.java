package com.deepLearning.security.controllers;

import com.deepLearning.security.model.User;
import com.deepLearning.security.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/secured")
//@RequiredArgsConstructor
public class CRUDController {

    private final UserRepository userRepo;

    public CRUDController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/get/${username}")
    public User getUserByUsername(@RequestParam String username) {
        return userRepo.findByUsername(username).orElseThrow();
    }


    @PostMapping("/save")
    public User saveUser(@RequestBody User user) {
        return userRepo.save(user);
    }

    @DeleteMapping("/delete/${username}")
    public boolean deleteUserByUsername(@RequestParam String username) {
        userRepo.deleteByUsername(username);
        return true;
    }


}
