package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentController {

    @Operation(summary = "Получение комментариев объявления" , responses = {
            @ApiResponse(responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Comments.class)
                    )),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = @Content()),
            @ApiResponse(responseCode = "404",
            description = "Not found",
            content = @Content())
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> get(@PathVariable Integer id){
        return ResponseEntity.ok(new Comments());
    }


    @Operation(summary = "Добавление комментария к объявлению" , responses = {
            @ApiResponse(responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Comment.class)
                    )),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = @Content()),
            @ApiResponse(responseCode = "404",
                    description = "Not found",
                    content = @Content())
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> create(@PathVariable Integer id, @RequestBody CreateOrUpdateComment newComment){
        return ResponseEntity.ok(new Comment());
    }


    @Operation(summary = "Удаление комментария" , responses = {
            @ApiResponse(responseCode = "200",
                    description = "OK",
                    content = @Content()),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = @Content()),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden",
                    content = @Content()),
            @ApiResponse(responseCode = "404",
                    description = "Not found",
                    content = @Content())
    })
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> delete(@PathVariable Integer adId, @PathVariable Integer commentId) {
        return ResponseEntity.ok().build();
    }



    @Operation(summary = "Обновление комментария" , responses = {
            @ApiResponse(responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Comment.class)
                    )),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = @Content()),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden",
                    content = @Content()),
            @ApiResponse(responseCode = "404",
                    description = "Not found",
                    content = @Content())
    })
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> update(@PathVariable Integer adId,
                                          @PathVariable Integer commentId,
                                          @RequestBody CreateOrUpdateComment newComment){
        return ResponseEntity.ok(new Comment());
    }
}
