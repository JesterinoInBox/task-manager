package com.jesterino.task_manager.service;

import com.jesterino.task_manager.Exception.ResourceNotFoundException;
import com.jesterino.task_manager.entity.User;
import com.jesterino.task_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User with id " + id + " not found"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id +" not found");
        }
        userRepository.deleteById(id);
    }

    public User updateUser(User updatedUser, Long id) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id +" not found"));
        existing.setName(updatedUser.getName());
        return userRepository.save(existing);
    }
}
