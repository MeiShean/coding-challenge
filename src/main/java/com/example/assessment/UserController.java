package com.example.assessment;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/user")
public class UserController {
    ArrayList<UserModel> user = new ArrayList<>();


    @GetMapping({"/user-list"})
    public ArrayList<UserModel> userList() {
        return this.user;
    }

    @PostMapping(path = {"/create-user"})
    public String createUser(@RequestBody UserModel user) {
        String errorMessage = "";
        boolean userEmailExist = this.user.stream().anyMatch((i) -> { return i.email.equalsIgnoreCase(user.email);});
        if (userEmailExist) {
            errorMessage = "Email has already existed";
        } else {
            user.id = this.user.size() == 0 ? 0 : user.id;
            this.user.add(user);
            return errorMessage;
        }

        return errorMessage;
    }

    @DeleteMapping(path = {"/delete-user-by-id/{id}"})
    public void deleteUserById(@PathVariable Integer id) {
        this.user.removeIf((i) -> {
            return i.id.equals(id);
        });
    }

    @PostMapping(path = {"update-user-by-id/{id}"})
    public String updateUserById(@PathVariable Integer id, @RequestBody UserModel user) {
        String errorMessage = "";
        boolean userEmailExist = this.user.stream().anyMatch((i) -> { return !i.id.equals(id) && i.email.equalsIgnoreCase(user.email);});
        if (userEmailExist) {
            errorMessage = "Email has already existed";
        } else {
            this.user.forEach((i) -> {
                if(i.id.equals(id)){
                    i.firstName = user.firstName;
                    i.lastName = user.lastName;
                    i.email = user.email;
                    i.avatar = user.avatar;
                }
            });
        }
        return errorMessage;
    }
}
