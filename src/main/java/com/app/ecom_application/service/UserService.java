package com.app.ecom_application.service;

import com.app.ecom_application.model.User;
import com.app.ecom_application.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


//write now i am creating direct class but letter i will create an interface and provide and modularity to code.
@Service
public class UserService {
    private final UserRepository userRepository;

   // private List<User> userList = new ArrayList<>();
   // private Long nextId=1L;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> fetchAllUsers(){
        return userRepository.findAll() ;
    }

    public void addUser(User user){
       userRepository.save(user);
    }

    public Optional<User> fetchUsersdetails(Long id){
          return userRepository.findById(id);
    }

    public  boolean UpdateUsersdetails(Long id, User updateedUser){
        return userRepository.findById(id)
                .map(existingUser ->{
                    existingUser.setFirstName(updateedUser.getFirstName());
                    existingUser.setLastName(updateedUser.getLastName());
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }
}
