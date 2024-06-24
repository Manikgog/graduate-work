package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.impl.UserServiceImpl;

@RequestMapping("/users")
@RestController
public class UserController {

    private UserService userService;

    public UserController(UserServiceImpl userService){
        this.userService = userService;
    }

    @PostMapping("/set_password")
    public ResponseEntity<HttpStatus> setPassword(@RequestBody NewPassword newPassword){
        return ResponseEntity.ok().build();
        //return userService.setNewPassword(newPassword) ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUser(){
        User user = new User();
        user.setId(1);
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmail("Email");
        user.setPhone("Phone");
        user.setImage("/image");
        user.setRole(Role.USER);
        return ResponseEntity.ok(user);
        //return ResponseEntity.ok(userService.getUser());
    }

    @PatchMapping("/me")
    public ResponseEntity<UserPatch> updateUser(@RequestBody UserPatch userPatch){
        return ResponseEntity.ok(userPatch);
        //return ResponseEntity.ok(userService.updateUser(userPatch));
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateImage(@RequestParam MultipartFile userPhoto) {
        User user = new User();
        user.setId(1);
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmail("Email");
        user.setPhone("Phone");
        user.setImage("/image");
        user.setRole(Role.USER);
        return ResponseEntity.ok(user);
        //return ResponseEntity.ok(userService.updateImage(userPhoto));
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegister> registerUser(@RequestBody UserRegister userRegister) {
        return ResponseEntity.ok(userRegister);
        //return ResponseEntity.ok(userService.registerUser(userRegister));
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> loginUser(@RequestBody UserAuthorisation userAuthorisation) {
        userService.loginUser(userAuthorisation);
        return ResponseEntity.ok().build();
    }

}
