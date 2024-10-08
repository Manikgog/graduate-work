package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.net.MalformedURLException;

@Slf4j
@RequestMapping("/users")
@RestController
@CrossOrigin("http://localhost:3000")
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
        log.info("The setPassword method of UserController is called");
        userService.setNewPassword(newPassword);
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
        log.info("The getUser method of UserController is called");
        return ResponseEntity.ok(userService.getUser());
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
        log.info("The updateUser method of UserController is called");
        return ResponseEntity.ok(userService.updateUser(userPatch));
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
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) throws IOException {
        log.info("The updateImage method of UserController is called");
        userService.updateImage(image);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение фотографии пользователя по его id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получение изображения пользователя по его id прошло успешно",
                            content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE
                            )
                    )
            }
    )
    @GetMapping(value = "/{id}/image")
    public ResponseEntity<?> getImage(@PathVariable("id") Long id, HttpServletResponse response) throws MalformedURLException {
        userService.getImage(id, response);
        return ResponseEntity.ok().build();
    }


}
