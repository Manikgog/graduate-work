package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    private final UserService userService;

    public UserController(UserServiceImpl userService){
        this.userService = userService;
    }

    @Tag(name = "Пользователи")
    @PostMapping("/set_password")
    public ResponseEntity<HttpStatus> setPassword(@RequestBody NewPassword newPassword){
        return ResponseEntity.ok().build();
        //return userService.setNewPassword(newPassword) ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Tag(name = "Пользователи")
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

    @Tag(name = "Пользователи")
    @PatchMapping("/me")
    public ResponseEntity<UserPatch> updateUser(@RequestBody UserPatch userPatch){
        return ResponseEntity.ok(userPatch);
        //return ResponseEntity.ok(userService.updateUser(userPatch));
    }

    @Tag(name = "Пользователи")
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

    @Operation(summary = "Регистрация нового пользователя" , responses = {
            @ApiResponse(responseCode = "200",
                    description = "Регистрация пользователя прошла успешно",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    name = "Пользователь",
                                    description = "Объект пользователя зарегистрирован"
                            )
                    ))
    })
    @Tag(name = "Регистрация")
    @PostMapping("/register")
    public ResponseEntity<UserRegister> registerUser(@RequestBody UserRegister userRegister) {
        return ResponseEntity.ok(userRegister);
        //return ResponseEntity.ok(userService.registerUser(userRegister));
    }

    @Operation(summary = "Авторизация пользователя", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Авторизация пользователя прошла успешно",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    name = "Пользователь",
                                    description = "Объект пользователя авторизован"
                            )
                    ))
    })
    @Tag(name = "Авторизация")
    @PostMapping("/login")
    public ResponseEntity<HttpStatus> loginUser(@RequestBody Login login) {
        userService.loginUser(login);
        return ResponseEntity.ok().build();
    }

}
