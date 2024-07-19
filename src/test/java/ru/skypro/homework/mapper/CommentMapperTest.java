package ru.skypro.homework.mapper;

import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

public class CommentMapperTest {

    private final Faker faker = new Faker();

    @Test
    public void commentEntityToComment() {
        UserEntity userEntity = createUser();
        AdEntity adEntity = createAd(userEntity);
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(faker.number().randomNumber());
        commentEntity.setAuthor(userEntity);
        commentEntity.setAd(adEntity);
        commentEntity.setCreatedAt(faker.number().randomNumber());
        commentEntity.setText(faker.book().title());

        Comment expected = createCommnet(commentEntity);

        Comment actual = CommentMapper.INSTANCE.commentEntityToComment(commentEntity);

        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

    }


    public UserEntity createUser() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail(faker.internet().emailAddress());
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setPhone(faker.phoneNumber().phoneNumber());
        user.setRole(Role.USER);
        user.setImage(faker.file().fileName());
        user.setPassword(faker.internet().password());
        return user;
    }

    public AdEntity createAd(UserEntity user) {
        AdEntity adEntity = new AdEntity();
        adEntity.setId(1L);
        adEntity.setAuthor(user);
        adEntity.setImage(faker.file().fileName());
        adEntity.setPrice(faker.number().numberBetween(1, 10000));
        adEntity.setTitle(faker.book().title());
        adEntity.setDescription(faker.expression(adEntity.getTitle()));
        return adEntity;
    }

    public Comment createCommnet(CommentEntity commentEntity) {
        Comment comment = new Comment();
        comment.setAuthor(commentEntity.getAuthor().getId());
        comment.setAuthorImage(commentEntity.getAuthor().getImage());
        comment.setAuthorFirstName(commentEntity.getAuthor().getFirstName());
        comment.setCreatedAt(commentEntity.getCreatedAt());
        comment.setPk(commentEntity.getId());
        comment.setText(commentEntity.getText());
        return comment;
    }
}
