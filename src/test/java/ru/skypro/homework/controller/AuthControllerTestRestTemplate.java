package ru.skypro.homework.controller;

import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.skypro.homework.constants.Constants.PHONE_PATTERN;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTestRestTemplate {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthController authController;

    @Autowired
    private UserRepo userRepo;

    private final Faker faker = new Faker();

    private final static int NUMBER_OF_USERS = 5;

    private final List<Register> users = new ArrayList<>();

    private final Pattern pat = Pattern.compile(PHONE_PATTERN);

    @BeforeEach
    public void beforeEach() {
        createUsers();
        for (Register user : users) {
            authController.register(user);
        }
    }

    @AfterEach
    public void afterEach() {
        removeUsers();
        userRepo.deleteAll();
    }

    private void removeUsers(){
       users.clear();
    }

    private void createUsers() {
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
    }

    private String getPhoneNumber(){
        String phoneNumber = faker.phoneNumber().cellPhone();
        while (true){
            if(phoneNumber.contains("(")) {
                String subStr =  phoneNumber.substring(0, phoneNumber.length() - 2);
                String subStr2 = phoneNumber.substring(phoneNumber.length() - 2);
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
    public void login_positiveTest() {
        UserEntity userEntity = userRepo.findAll().stream().findAny().get();
        String password = users.stream().filter(u -> u.getUsername().equals(userEntity.getEmail())).findFirst().get().getPassword();
        Login login = new Login();
        login.setUsername(userEntity.getEmail());
        login.setPassword(password);

        ResponseEntity<String> response = restTemplate.withBasicAuth(login.getUsername(), password).postForEntity(
                "http://localhost:" + port + "/login",
                login,
                String.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void login_negativeTest() {
        Login login = new Login();
        login.setUsername("user");
        login.setPassword("password");

        ResponseEntity<String> response = restTemplate.withBasicAuth("user1", "password1").postForEntity(
                "http://localhost:" + port + "/login",
                login,
                String.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void register_positiveTest() {
        Register register = new Register();
        register.setFirstName(faker.name().firstName());
        register.setLastName(faker.name().lastName());
        register.setUsername(faker.internet().emailAddress());
        register.setPassword(faker.internet().password());
        register.setPhone(getPhoneNumber());
        register.setRole(Role.USER);

        int expectedSize = NUMBER_OF_USERS + 1;

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/register",
                register,
                String.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(userRepo.findAll().size()).isEqualTo(expectedSize);
        Assertions.assertThat(userRepo.findByEmail(register.getUsername()).get().getEmail()).isEqualTo(register.getUsername());
    }

    @Test
    public void register_negativeTestByWrongPhone() {
        Register register = new Register();
        register.setFirstName(faker.name().firstName());
        register.setLastName(faker.name().lastName());
        register.setUsername(faker.internet().emailAddress());
        register.setPassword(faker.internet().password());
        register.setPhone("123456789");
        register.setRole(Role.USER);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/register",
                register,
                String.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void register_negativeTestByIsExistsUserName() {
        Register register = new Register();
        UserEntity userEntity = userRepo.findAll().stream().findAny().get();
        register.setUsername(userEntity.getEmail());

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/register",
                register,
                String.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
