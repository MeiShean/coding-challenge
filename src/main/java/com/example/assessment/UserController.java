package com.example.assessment;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/user")
public class UserController {
    ArrayList<UserModel> user = new ArrayList<>();

    @GetMapping("/user-list")
    public ArrayList<UserModel> userList() {
        return this.user;
    }

    @PostMapping(path = "/create-user")
    public String createUser(@RequestBody UserModel user){
        String errorMessage = "";
        if(this.user.stream().anyMatch(i-> i.email.equalsIgnoreCase(user.email))){
            errorMessage = "Email has already existed";
            return errorMessage;
        }
        else{
            user.id = this.user.size() == 0 ? 0 : this.user.size();
            this.user.add(user);
        }
        return errorMessage;
    }

    @DeleteMapping(path = "/delete-user-by-id/{id}")
    public void deleteUserById(@PathVariable Integer id){
        System.out.println("halo==   " + id);
    }
}
