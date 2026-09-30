package com.app.ecom_application.controller;

import com.app.ecom_application.model.User;
import com.app.ecom_application.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.fetchAllUsers());
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user){
         userService.addUser(user);

         return ResponseEntity.ok( "User Added succesfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUsersdetails(@PathVariable Long id){

//        User user= userService.fetchUsersdetails(id);
//        if(user==null){
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(user);


        //  JAVA8 Features.
        return userService.fetchUsersdetails(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->  ResponseEntity.notFound().build());
    }

    //write controller to update and delete the records
    @PutMapping("/{id}")
    public ResponseEntity<String> createUser(@PathVariable Long id, @RequestBody User updatedUser){
        boolean updated=userService.UpdateUsersdetails(id,updatedUser);
        if(updated)
             return ResponseEntity.ok("User updated Suceessfully");
        return ResponseEntity.notFound().build();
    }

}
