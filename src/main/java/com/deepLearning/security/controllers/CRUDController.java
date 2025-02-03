package com.deepLearning.security.controllers;

import com.deepLearning.security.model.UserEntity;
import com.deepLearning.security.repositories.UserRepo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/secured")
//@RequiredArgsConstructor
public class CRUDController {

    private final UserRepo userRepo;

    public CRUDController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/get/${username}")
    public UserEntity getUserByUsername(@RequestParam String username) {
        return userRepo.findById(username).orElseThrow();
    }


    @PostMapping("/save")
    public UserEntity saveUser(@RequestBody UserEntity user) {
        return userRepo.save(user);
    }

    @DeleteMapping("/delete/${username}")
    public boolean deleteUserByUsername(@RequestParam String username) {
        userRepo.deleteById(username);
        return true;
    }

    @GetMapping("/test")
    public String test() {
        return "secured admin test";
    }


}
