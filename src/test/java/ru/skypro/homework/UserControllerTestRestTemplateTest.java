package ru.skypro.homework;

import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.skypro.homework.config.WebSecurityConfig;
import ru.skypro.homework.controller.AuthController;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepo;
import ru.skypro.homework.service.UserService;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.skypro.homework.constants.Constants.PHONE_PATTERN;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTestRestTemplateTest {
    @Value("${path.to.userImages.folder}")
    private String userImagesFolder;

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
    private UserService userService;

    @Autowired
    private WebSecurityConfig webSecurityConfig;

    @Autowired
    private AuthController authController;

    @Autowired
    private UserMapper userMapper;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final Pattern pat = Pattern.compile(PHONE_PATTERN);

    private final Faker faker = new Faker();

    private final static int NUMBER_OF_USERS = 5;

    private List<Register> users = new ArrayList<>();

    @BeforeEach
    public void beforeEach() {
        createUsers();
        for (int i = 0; i < users.size(); i++) {
            authController.register(users.get(i));
        }
    }

    @AfterEach
    public void afterEach() {
        List<String> fileNames = userRepo.findAll().stream().filter(u -> u.getImage() != null).map(UserEntity::getImage).toList();
        for (int i = 0; i < fileNames.size(); i++) {
            Path filePath = Paths.get(userImagesFolder, fileNames.get(i));
            filePath.toFile().deleteOnExit();
        }
        removeUsers();
        userRepo.deleteAll();
    }

    private void removeUsers(){
        for (int i = 0; i < users.size(); i++) {
            users.remove(i);
        }
    }

    private List<Register> createUsers() {
        for (int i = 0; i < NUMBER_OF_USERS; i++) {
            Register register = new Register();
            register.setFirstName(faker.name().firstName());
            register.setLastName(faker.name().lastName());
            register.setUsername(faker.internet().emailAddress());
            register.setPassword(faker.internet().password());
            register.setPhone(getPhoneNumber());
            if(i == NUMBER_OF_USERS-1){
                register.setRole(Role.ADMIN);
            }else {
                register.setRole(Role.USER);
            }
            users.add(register);
        }
        return users;
    }

    private String getPhoneNumber(){
        String phoneNumber = faker.phoneNumber().cellPhone();
        while (true){
            if(phoneNumber.contains("(")) {
                String subStr =  phoneNumber.substring(0, phoneNumber.length() - 2);
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

    @Test
    public void setPassword_positiveTest() throws Exception {
        UserEntity userEntity = userRepo.findAll().stream().findAny().get();
        NewPassword newPassword = new NewPassword();
        newPassword.setNewPassword("password");
        newPassword.setCurrentPassword(users.stream().filter(u -> u.getUsername().equals(userEntity.getEmail())).findFirst().get().getPassword());

       ResponseEntity<String> response = restTemplate.withBasicAuth(userEntity.getEmail(), newPassword.getCurrentPassword()).postForEntity(
                "http://localhost:" + port + "/users/set_password",
                newPassword,
                String.class
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserEntity userEntityWithNewPassword = userRepo.findByEmail(userEntity.getEmail()).orElse(null);
        String newEncodedPassword = userEntityWithNewPassword.getPassword();

        boolean equals = encoder.matches("password", newEncodedPassword);
        Assertions.assertThat(equals).isEqualTo(true);
    }

    @Test
    public void setPassword_negativeTest() {
        NewPassword newPassword = new NewPassword();
        ResponseEntity<String> response = restTemplate.withBasicAuth("user", "password").postForEntity(
                "http://localhost:" + port + "/users/set_password",
                newPassword,
                String.class
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void getUser_positiveTest() {
        UserEntity userEntity = userRepo.findAll().stream().findAny().get();

        String notEndodedPassword = users.stream().filter(u -> u.getUsername().equals(userEntity.getEmail())).findFirst().get().getPassword();

        ResponseEntity<User> response = restTemplate.withBasicAuth(userEntity.getEmail(), notEndodedPassword).getForEntity(
                "http://localhost:" + port + "/users/me",
                User.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        User user = userMapper.toUser(userEntity);

        User userFromResponse = response.getBody();
        Assertions.assertThat(userFromResponse.getId()).isEqualTo(user.getId());
        Assertions.assertThat(userFromResponse.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(userFromResponse.getPhone()).isEqualTo(user.getPhone());
        Assertions.assertThat(userFromResponse.getFirstName()).isEqualTo(user.getFirstName());
        Assertions.assertThat(userFromResponse.getLastName()).isEqualTo(user.getLastName());
        Assertions.assertThat(userFromResponse.getRole()).isEqualTo(user.getRole());
    }

    @Test
    public void updateUser_positiveTest() {
        UserEntity userEntity = userRepo.findAll().stream().findAny().get();
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName(faker.name().firstName());
        updateUser.setLastName(userEntity.getLastName());
        updateUser.setPhone(userEntity.getPhone());
        String notEndodedPassword = users.stream().filter(u -> u.getUsername().equals(userEntity.getEmail())).findFirst().get().getPassword();

        ResponseEntity<UpdateUser> response = restTemplate.withBasicAuth(userEntity.getEmail(), notEndodedPassword).exchange(
                "http://localhost:" + port + "/users/me",
                HttpMethod.PATCH,
                new HttpEntity<>(updateUser),
                UpdateUser.class,
                Map.of()
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(updateUser);

    }

    @Test
    public void uploadImage_positiveTest() {
        UserEntity userEntity = userRepo.findAll().stream().findAny().get();
        String userName = userEntity.getEmail();
        String notEndodedPassword = users.stream().filter(u -> u.getUsername().equals(userEntity.getEmail())).findFirst().get().getPassword();
        Path filePath = Paths.get(userImagesFolder, "1.jpg");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        FileSystemResource fileSystemResource = new FileSystemResource(filePath);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", fileSystemResource);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.withBasicAuth(userName, notEndodedPassword).exchange(
                "http://localhost:" + port + "/users/me/image",
                HttpMethod.PATCH,
                requestEntity,
                String.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(userRepo.findByEmail(userName).get().getImage()).isNotEmpty();
    }

    @Test
    public void getImage_positiveTest() {
        UserEntity userEntity = userRepo.findAll().stream().findAny().get();
        String userName = userEntity.getEmail();
        String notEndodedPassword = users.stream().filter(u -> u.getUsername().equals(userEntity.getEmail())).findFirst().get().getPassword();

        uploadImage(userName, notEndodedPassword);
        UserEntity userEntityWithImage = userRepo.findByEmail(userEntity.getEmail()).get();
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth(userName, notEndodedPassword).exchange(
                "http://localhost:" + port + "/users/{id}/image",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class,
                Map.of("id", userEntity.getId())
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    private void uploadImage(String username, String password){
        Path filePath = Paths.get(userImagesFolder, "1.jpg");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        FileSystemResource fileSystemResource = new FileSystemResource(filePath);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", fileSystemResource);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.withBasicAuth(username, password).exchange(
                "http://localhost:" + port + "/users/me/image",
                HttpMethod.PATCH,
                requestEntity,
                String.class
        );
    }

}
