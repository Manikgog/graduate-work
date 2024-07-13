package ru.skypro.homework.check;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.skypro.homework.entity.*;
import ru.skypro.homework.exceptions.EntityNotFoundException;
import ru.skypro.homework.repository.AdRepo;
import ru.skypro.homework.repository.CommentRepo;
import ru.skypro.homework.repository.UserRepo;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckAccessService {
    private final UserRepo userRepo;
    private final CommentRepo commentRepo;
    private final AdRepo adRepo;

    public boolean isAdminOrOwnerComment(Long adId, Long commentId, Authentication authentication) {
        log.info("Was invoked method for verify of access");
        UserEntity userEntity = userRepo.findByEmail(authentication.getName())
                .orElseThrow(() -> {
                    log.error("An EntityNotFoundException " + "(User " + authentication.getName() + " not found)" + "exception was thrown when calling the isAdminOrOwnerComment method of CheckAccessService");
                    return new EntityNotFoundException("User " + authentication.getName() + " not found");
                });
        AdEntity adEntity = adRepo.findById(adId)
                .orElseThrow(() -> {
                    log.error("An EntityNotFoundException " + "(Ad " + adId + " not found)" + "exception was thrown when calling the isAdminOrOwnerComment method of CheckAccessService");
                    return new EntityNotFoundException("Ad " + adId + " not found");
                });

        CommentEntity commentEntity = commentRepo.findById(commentId)
                .orElseThrow(() -> {
                    log.error("An EntityNotFoundException " + "(Comment " + commentId + " not found)" + "exception was thrown when calling the isAdminOrOwnerComment method of CheckAccessService");
                    return new EntityNotFoundException("Comment " + commentId + " not found");
                });
        return userEntity.getRole().name().equals("ADMIN")
                || (adEntity.equals(commentEntity.getAd())
                && userEntity.getId().equals(commentEntity.getAuthor().getId()));
    }

    public boolean isAdminOrOwnerAd(Long adId, Authentication authentication) {
        log.info("Was invoked method for verify of access");
        UserEntity userEntity = userRepo.findByEmail(authentication.getName())
                .orElseThrow(() -> {
                    log.error("An EntityNotFoundException " + "(User " + authentication.getName() + " not found)" + "exception was thrown when calling the isAdminOrOwnerAd method of CheckAccessService");
                    return new EntityNotFoundException("User " + authentication.getName() + " not found");
                });
        AdEntity adEntity = adRepo.findById(adId)
                .orElseThrow(() -> {
                    log.error("An EntityNotFoundException " + "(Ad " + adId + " not found)" + "exception was thrown when calling the isAdminOrOwnerAd method of CheckAccessService");
                    return new EntityNotFoundException("Ad " + adId + " not found");
                });
        return userEntity.getRole().name().equals("ADMIN")
                || userEntity.getId().equals(adEntity.getAuthor().getId());
    }
}