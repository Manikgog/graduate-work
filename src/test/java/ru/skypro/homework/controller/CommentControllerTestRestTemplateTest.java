package ru.skypro.homework.controller;

import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepo;
import ru.skypro.homework.repository.CommentRepo;
import ru.skypro.homework.repository.UserRepo;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.skypro.homework.constants.Constants.PHONE_PATTERN;
import static ru.skypro.homework.dto.Role.USER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTestRestTemplateTest {

    @Value("${path.to.userImages.folder}")
    private String userImagesFolder;
    @Value("${path.to.adImages.folder}")
    private String adImagesFolder;
    @LocalServerPort
    private int port;
    @Value("test_user_images")
    private String userImages;
    @Value("test_ad_images")
    private String adImages;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private AdRepo adRepo;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private AuthController authController;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final Pattern pat = Pattern.compile(PHONE_PATTERN);
    private final Faker faker = new Faker();
    private final static int NUMBER_OF_COMMENTS = 3;
    private final List<CommentEntity> commentEntities = new ArrayList<>();
    UserEntity userEntity = new UserEntity();
    private Register register = new Register();

    @BeforeEach
    public void beforeEach() {
        createUserEntity();
        createAdEntity();
        createCommentEntities();

        register.setFirstName(userEntity.getFirstName());
        register.setLastName(userEntity.getLastName());
        register.setUsername(userEntity.getEmail());
        register.setPassword(userEntity.getPassword());
        register.setPhone(userEntity.getPhone());
        register.setRole(userEntity.getRole());
        authController.register(register);
    }

    @AfterEach
    public void afterEach() {

        commentEntities.clear();
        commentRepo.deleteAll();
        adRepo.deleteAll();
        userRepo.deleteAll();
    }

    private String getPhoneNumber() {
        String phoneNumber = faker.phoneNumber().cellPhone();
        while (true) {
            if (phoneNumber.contains("(")) {
                String subStr = phoneNumber.substring(0, phoneNumber.length() - 2);
                String subStr2 = phoneNumber.substring(phoneNumber.length() - 2, phoneNumber.length());
                phoneNumber = "+7 " + subStr + "-" + subStr2;
                Matcher mat = pat.matcher(phoneNumber);
                if (mat.matches()) {
                    return phoneNumber;
                }
            }
            phoneNumber = faker.phoneNumber().cellPhone();
        }
    }

    private AdEntity createAdEntity() {
        AdEntity adEntity = new AdEntity();
        adEntity.setPrice(100000);
        adEntity.setImage("1.jpg");
        adEntity.setTitle(faker.text().text(30));
        adEntity.setDescription(faker.text().text(30));
        adEntity.setAuthor(userRepo.findAll().stream().findAny().get());
        return adRepo.save(adEntity);
    }

    private List<CommentEntity> createCommentEntities() {
        for (int i = 0; i <= NUMBER_OF_COMMENTS; i++) {
            CommentEntity commentEntity = new CommentEntity();
            commentEntity.setText(faker.text().text(30));
            commentEntity.setAd(adRepo.findAll().stream().findAny().get());
            commentEntity.setAuthor(createUserEntity());
            commentEntity.setCreatedAt(faker.time().past(100000, ChronoUnit.MILLIS));
            commentEntities.add(commentEntity);
        }
        return commentRepo.saveAll(commentEntities);

    }

    private UserEntity createUserEntity() {
        userEntity.setEmail("USER@mail.ru");
        userEntity.setPassword("password");
        userEntity.setRole(USER);
        userEntity.setPhone(getPhoneNumber());
        userEntity.setFirstName("Alex");
        userEntity.setLastName("Novik");
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        return userRepo.save(userEntity);
    }

    @Test
    void getPositiveTest() {
        AdEntity adEntity = adRepo.findAll().stream().findAny().get();
        ResponseEntity<Comments> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/ads/{id}/comments",
                Comments.class,
                Map.of("id", adEntity.getId())
        );
        List<Comment> commentList = commentRepo.findByAdId(adEntity.getId())
                .stream().map(commentMapper::commentEntityToComment)
                .toList();
        for (Comment item : commentList) {
            item.setAuthorImage("/users/" + item.getAuthor() + "/image");
        }
        Comments comments = new Comments();
        comments.setCount(commentList.size());
        comments.setResults(commentList);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody()).isEqualTo(comments);

    }

    @Test
    void createPositiveTest() {
        AdEntity adEntity = adRepo.findAll().stream().findAny().get();
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText("TextOfComment");
        CommentEntity commentEntity = commentRepo.findAll().stream().findAny().get();
        commentEntity.setText(createOrUpdateComment.getText());
        HttpEntity<CreateOrUpdateComment> commentHttpEntity = new HttpEntity<>(createOrUpdateComment);
        ResponseEntity<Comment> response = restTemplate.withBasicAuth(userEntity.getEmail(), "password").postForEntity(
                "http://localhost:" + port + "/ads/{id}/comments",
                commentHttpEntity,
                Comment.class,
                Map.of("id", adEntity.getId()
                ));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getText()).isEqualTo(createOrUpdateComment.getText());
    }

    @Test
    void createNegativeTest() {
        AdEntity adEntity = adRepo.findAll().stream().findAny().get();
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText("TextOfComment");
        HttpEntity<CreateOrUpdateComment> commentHttpEntity = new HttpEntity<>(createOrUpdateComment);
        ResponseEntity<Comment> response = restTemplate.withBasicAuth(userEntity.getEmail(), "passwordd").postForEntity(
                "http://localhost:" + port + "/ads/{id}/comments",
                commentHttpEntity,
                Comment.class,
                Map.of("id", adEntity.getId()
                ));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void deletePositiveTest() {
        AdEntity adEntity = adRepo.findAll().stream().findAny().get();
        CommentEntity commentEntity = commentRepo.findAll().stream().findAny().get();
        ResponseEntity<Void> response = restTemplate.withBasicAuth(userEntity.getEmail(), "password").exchange(
                "http://localhost:" + port + "/ads/{adId}/comments/{commentId}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                Map.of("adId", adEntity.getId(),
                        "commentId", commentEntity.getId())
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        List<CommentEntity> expected = commentRepo.findAll();
//        assertThat(expected.size()).isEqualTo(commentEntities.size() - 1);
    }
    @Test
    void deleteNegativeTest() {
        AdEntity adEntity = adRepo.findAll().stream().findAny().get();
        CommentEntity commentEntity = commentRepo.findAll().stream().findAny().orElseThrow();
        ResponseEntity<?> response = restTemplate.withBasicAuth(userEntity.getEmail(), "password").exchange(
                "http://localhost:" + port + "/ads/{adId}/comments/{commentId}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                Map.of("adId", (adEntity.getId()+1),
                        "commentId", commentEntity.getId())
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updatePositiveTest() {
        AdEntity adEntity = adRepo.findAll().stream().findAny().get();
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText("TextOfComment");
        CommentEntity commentEntity = commentRepo.findAll().stream().findAny().get();
        commentEntity.setCreatedAt(Instant.now().toEpochMilli());
        commentEntity.setAd(adEntity);
        commentEntity.setText(createOrUpdateComment.getText());
        commentEntity.setAuthor(userEntity);
        HttpEntity<CreateOrUpdateComment> commentHttpEntity = new HttpEntity<>(createOrUpdateComment);
        ResponseEntity<Comment> response = restTemplate.withBasicAuth(userEntity.getEmail(), "password").exchange(
                "http://localhost:" + port + "/ads/{adId}/comments/{commentId}",
                HttpMethod.PATCH,
                commentHttpEntity,
                Comment.class,
                Map.of("adId", adEntity.getId(),
                        "commentId", commentEntity.getId()
                ));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getText()).isEqualTo(createOrUpdateComment.getText());
    }
}