package com.example.book_storage_service.services;

import com.example.book_storage_service.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.book_storage_service.models.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    final private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> userByName(String name){
        return userRepository.findByName(name);
    }

    public Optional<User> userById(Long id){
        return userRepository.findById(id);
    }

    public List<User> allUsers(){
        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void deleteUserById(Long id){
        Optional<User> user = userRepository.findById(id);

        user.ifPresentOrElse(u ->{
            userRepository.deleteById(u.getId());
        }, ()->{
            System.out.println("While deleting: user with id " + id +" have not found");
        });
    }


}
