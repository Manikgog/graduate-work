package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

@Slf4j
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Комментарии")
@CrossOrigin("http://localhost:3000")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Получение комментариев объявления", responses = {
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
    public ResponseEntity<Comments> get(@PathVariable("id") Long id) {
        log.info("The get method of CommentController is called");
        return ResponseEntity.ok(commentService.get(id));
    }


    @Operation(summary = "Добавление комментария к объявлению", responses = {
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
    public ResponseEntity<Comment> create(@PathVariable("id") Long id,
                                          @RequestBody CreateOrUpdateComment newComment) {
        log.info("The create method of CommentController is called");
        return ResponseEntity.ok(commentService.create(id, newComment));
    }


    @Operation(summary = "Удаление комментария", responses = {
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
    @PreAuthorize("@checkAccessService.isAdminOrOwnerComment(#adId, #commentId, authentication)")
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> delete(@PathVariable("adId") Long adId,
                                    @PathVariable("commentId") Long commentId) {
        log.info("The delete method of CommentController is called");
        commentService.delete(adId, commentId);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Обновление комментария", responses = {
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
    @PreAuthorize("@checkAccessService.isAdminOrOwnerComment(#adId, #commentId, authentication)")
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> update(@PathVariable(value = "adId") Long adId,
                                          @PathVariable(value = "commentId") Long commentId,
                                          @RequestBody CreateOrUpdateComment newComment) {
        log.info("The update method of CommentController is called");
        return ResponseEntity.ok(commentService.update(adId, commentId, newComment));
    }
}
