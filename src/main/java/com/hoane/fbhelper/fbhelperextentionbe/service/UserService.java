package com.hoane.fbhelper.fbhelperextentionbe.service;


import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.exception.UserNotFoundException;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public User findById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

}
