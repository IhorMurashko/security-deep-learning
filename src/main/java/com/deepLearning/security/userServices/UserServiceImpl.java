package com.deepLearning.security.userServices;

import com.deepLearning.security.model.User;
import com.deepLearning.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public User save(@NonNull User user) {

        return userRepo.save(user);
    }

    @Override
    public Optional<User> findUserByUsername(@NonNull String username) {


        return userRepo.findByUsername(username);
    }

    @Override
    public Optional<User> findUserById(@NonNull Long id) {

        return userRepo.findById(id);
    }

    @Override
    public void deleteUserById(@NonNull Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public void deleteByUsername(@NonNull String username) {
        userRepo.deleteByUsername(username);

    }

    @Override
    public boolean isExistUsername(@NonNull String username) {
        return userRepo.existsUserByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
