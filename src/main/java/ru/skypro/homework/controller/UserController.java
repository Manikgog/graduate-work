package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

@RequestMapping("/users")
@RestController
@Tag(name = "Пользователи")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Operation(summary = "Обновление пароля" , responses = {
            @ApiResponse(responseCode = "200",
                    description = "OK",
                    content = @Content()),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = @Content()),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden",
                    content = @Content())
    })
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение информации об авторизованном пользователе" , responses = {
            @ApiResponse(responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class)
                    )),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
            content = @Content())
    })

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
    }

    @Operation(summary = "Обновление информации об авторизованном пользователе",
            responses = {
            @ApiResponse(responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateUser.class)
                    )),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = @Content())
    })
    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser userPatch){
        return ResponseEntity.ok(new UpdateUser());
    }

    @Operation(summary = "Обновление аватара авторизованного пользователя" , responses = {
            @ApiResponse(responseCode = "200",
                    description = "OK",
                    content = @Content()),
            @ApiResponse(responseCode = "401",
            description = "Unauthorized",
                    content = @Content())
    })
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@RequestParam MultipartFile image) {
        return ResponseEntity.ok().build();
    }

}
