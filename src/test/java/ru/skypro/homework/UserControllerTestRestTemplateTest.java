package ru.skypro.homework;

import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.skypro.homework.config.WebSecurityConfig;
import ru.skypro.homework.controller.AuthController;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepo;
import ru.skypro.homework.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.skypro.homework.constants.Constants.PHONE_PATTERN;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTestRestTemplateTest {
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
    public void setPassword_positiveTest(){
        UserEntity userEntity = userRepo.findAll().stream().findAny().get();
        NewPassword newPassword = new NewPassword();
        newPassword.setNewPassword("password");
        newPassword.setCurrentPassword(users.stream().filter(u -> u.getUsername().equals(userEntity.getEmail())).findFirst().get().getPassword());

        ResponseEntity<String> response = restTemplate.withBasicAuth(userEntity.getEmail(), userEntity.getPassword()).postForEntity(
                "http://localhost:" + port + "/set_password",
                newPassword,
                String.class
        );
        System.out.println(response.getStatusCode());
    }
}
