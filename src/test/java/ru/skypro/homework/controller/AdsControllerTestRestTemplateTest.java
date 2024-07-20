package ru.skypro.homework.controller;

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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdEntityToExtendedAdMapper;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepo;
import ru.skypro.homework.repository.UserRepo;
import ru.skypro.homework.service.impl.AdsServiceImpl;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.skypro.homework.constants.Constants.PHONE_PATTERN;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdsControllerTestRestTemplateTest {
    @Value("${path.to.adImages.folder}")
    private String adImagesFolder;
    @Value("test_ad_images")
    private String adImages;
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AdRepo adRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AdMapper adMapper;
    @Autowired
    private AdsServiceImpl adsService;
    @Autowired
    private AuthController authController;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final Pattern pattern = Pattern.compile(PHONE_PATTERN);
    private final Faker faker = new Faker();
    private final static int COUNT_AD = 10;
    private final static int COUNT_USER = 5;

    private final List<AdEntity> listAd = new ArrayList<>();
    private final List<UserEntity> listUser = new ArrayList<>();
    @Autowired
    private AdEntityToExtendedAdMapper adEntityToExtendedAdMapper;
    @Autowired
    private TestRestTemplate testRestTemplate;

    private String buildUrl(String url) {
        return "http://localhost:%d%s".formatted(port, url);
    }

    @BeforeEach
    public void setUp() {
        createUserEntity();
        createAdEntity();
    }

    @AfterEach
    void tearDown() {
        listAd.clear();
        listUser.clear();
        List<String> fileNames = adRepo.findAll().stream().map(AdEntity::getImage).filter(Objects::nonNull).toList();
        for (String fileName : fileNames) {
            Path filePath = Paths.get(adImagesFolder, fileName);
            filePath.toFile().deleteOnExit();
        }
        adRepo.deleteAll();
        userRepo.deleteAll();
    }

    private void createAdEntity() {
        for (int i = 0; i < COUNT_AD; i++) {
            AdEntity adEntity = new AdEntity();
            adEntity.setAuthor(listUser.get(faker.random().nextInt(0, listUser.size() - 1)));
            adEntity.setTitle(faker.name().firstName());
            adEntity.setImage("/здесь должен быть путь к картинке/");
            adEntity.setPrice(faker.number().numberBetween(1, 100));
            adEntity.setDescription(faker.name().firstName());
            listAd.add(adEntity);
            adRepo.save(adEntity);
        }
    }

    private void createUserEntity() {
        for (int i = 0; i < COUNT_USER; i++) {
            UserEntity userEntity = new UserEntity();
            userEntity.setFirstName(faker.name().firstName());
            userEntity.setLastName(faker.name().lastName());
            String email = faker.internet().emailAddress();
            while (email.length() > 30){
                email = faker.internet().emailAddress();
            }
            userEntity.setEmail(email);
            userEntity.setPassword("password");
            userEntity.setPhone(getPhoneNumber());
            if (i == COUNT_USER - 1) {
                userEntity.setRole(Role.ADMIN);
            } else {
                userEntity.setRole(Role.USER);
            }
            listUser.add(userEntity);
            userEntity.setPassword(encoder.encode(userEntity.getPassword()));
            userRepo.save(userEntity);
        }
    }

    private String getPhoneNumber() {
        String phoneNumber = faker.phoneNumber().cellPhone();
        while (true) {
            if (phoneNumber.contains("(")) {
                String subStr = phoneNumber.substring(0, phoneNumber.length() - 2);
                String subStr2 = phoneNumber.substring(phoneNumber.length() - 2, phoneNumber.length());
                phoneNumber = "+7 " + subStr + "-" + subStr2;
                Matcher mat = pattern.matcher(phoneNumber);
                if (mat.matches()) {
                    return phoneNumber;
                }
            }
            phoneNumber = faker.phoneNumber().cellPhone();
        }
    }


    @Test
    void getAdsPositiveTest() {
        List<Ad> adList = adRepo.findAll().stream().map(adMapper::adEntityToAd).toList();
        Ads ads = new Ads();
        ads.setCount(adList.size());
        ads.setResults(adList);
        ResponseEntity<Ads> getAdFromDb = restTemplate
                .getForEntity(buildUrl("/ads"), Ads.class);
        Ads get = getAdFromDb.getBody();
        assertThat(getAdFromDb.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get).isNotNull();
        assertThat(get.getCount()).isEqualTo(adList.size());
        assertThat(get.getResults()).usingRecursiveComparison().ignoringFields("image").isEqualTo(adList);
    }

    @Test
    void createAdPositiveTest() {
        UserEntity userEntity = listUser.get(faker.random().nextInt(0, listUser.size() - 1));
        Path filePath = Paths.get(adImages, "2.jpg");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Content_Type", MediaType.APPLICATION_JSON_VALUE);
        FileSystemResource fileSystemResource = new FileSystemResource(filePath);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", fileSystemResource);
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setDescription("Описание какого то товара");
        createOrUpdateAd.setPrice(faker.random().nextInt(0, 100));
        createOrUpdateAd.setTitle("Какой то товар");
        body.add("properties", createOrUpdateAd);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Ad> createAd = restTemplate.withBasicAuth(userEntity.getEmail(), "password")
                .postForEntity(buildUrl("/ads"),
                        requestEntity,
                        Ad.class);

        Ad get = createAd.getBody();
        assertThat(createAd.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(get).isNotNull();
        assertThat(get.getTitle()).isEqualTo(createOrUpdateAd.getTitle());
        assertThat(true).isEqualTo(adRepo.findByTitle(createOrUpdateAd.getTitle()).isPresent());
    }

    @Test
    void getAdsExtendedPositiveTest() {
        AdEntity ad = listAd.get(faker.random().nextInt(listAd.size() - 1));
        UserEntity user = ad.getAuthor();
        ExtendedAd extendedAd = adEntityToExtendedAdMapper.perform(ad);
        ResponseEntity<ExtendedAd> getAdsExtended = restTemplate.withBasicAuth(user.getEmail(), "password")
                .getForEntity(buildUrl("/ads/" + ad.getId()), ExtendedAd.class);

        assertThat(getAdsExtended.getStatusCode()).isEqualTo(HttpStatus.OK);
        ExtendedAd getAd = getAdsExtended.getBody();
        assertThat(getAd).isNotNull();
        assertThat(getAd).usingRecursiveComparison().ignoringFields("image").isEqualTo(extendedAd);
    }

    @Test
    void getAdsExtendedNegative_IfAdNotFoundTest() {
        AdEntity ad = listAd.get(faker.random().nextInt(listAd.size() - 1));
        UserEntity user = ad.getAuthor();
        ExtendedAd extendedAd = adEntityToExtendedAdMapper.perform(ad);
        ResponseEntity<String> getAdsExtended = restTemplate.withBasicAuth(user.getEmail(), "password")
                .getForEntity(buildUrl("/ads/-1"), String.class);

        assertThat(getAdsExtended.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteAdsPositiveTest() {
        AdEntity ad = listAd.get(faker.random().nextInt(listAd.size() - 1));
        ResponseEntity<Void> removeEntity = testRestTemplate.withBasicAuth(ad.getAuthor().getEmail(), "password")
                .exchange(buildUrl("/ads/{id}"),
                        HttpMethod.DELETE,
                        null,
                        Void.class,
                        Map.of("id", ad.getId()));
        assertThat(removeEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<AdEntity> expected = adRepo.findAll();
        assertThat(expected).size().isEqualTo(listAd.size() - 1);
    }

    @Test
    void deleteAdsNegativeTest() {
        AdEntity ad = listAd.get(faker.random().nextInt(listAd.size() - 1));
        ResponseEntity<Void> removeEntity = testRestTemplate.withBasicAuth(ad.getAuthor().getEmail(), "password")
                .exchange(buildUrl("/ads" + ad.getId()),
                        HttpMethod.DELETE,
                        null,
                        Void.class);
        assertThat(removeEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void deleteAdsNegativeTest_IfUserUnauthorizedTest() {
        AdEntity ad = listAd.get(faker.random().nextInt(listAd.size() - 1));
        ResponseEntity<Void> removeEntity = testRestTemplate.withBasicAuth(ad.getAuthor().getEmail(), "password1")
                .exchange(buildUrl("/ads/" + ad.getId()),
                        HttpMethod.DELETE,
                        null,
                        Void.class);
        assertThat(removeEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void deleteAdsNegativeTest_IfAdNotFoundTest() {
        AdEntity ad = listAd.get(faker.random().nextInt(listAd.size() - 1));
        ResponseEntity<Void> removeEntity = testRestTemplate.withBasicAuth(ad.getAuthor().getEmail(), "password")
                .exchange(buildUrl("/ads/{id}"),
                        HttpMethod.DELETE,
                        null,
                        Void.class,
                        Map.of("id", -1));
        assertThat(removeEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateAdsPositiveTest() {
        AdEntity ad = listAd.get(faker.random().nextInt(listAd.size() - 1));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setDescription("Описание какого то товара");
        createOrUpdateAd.setPrice(faker.random().nextInt(0, 100));
        createOrUpdateAd.setTitle("Какой то товар");
        ad.setDescription(createOrUpdateAd.getDescription());
        ad.setPrice(createOrUpdateAd.getPrice());
        ad.setTitle(createOrUpdateAd.getTitle());
        Ad expectedAd = adMapper.adEntityToAd(ad);
        HttpEntity<CreateOrUpdateAd> entityAd = new HttpEntity<>(createOrUpdateAd);
        ResponseEntity<Ad> updateAd = testRestTemplate.withBasicAuth(ad.getAuthor().getEmail(), "password")
                .exchange(buildUrl("/ads/{id}"),
                        HttpMethod.PATCH,
                        entityAd,
                        Ad.class,
                        Map.of("id", ad.getId()));
        assertThat(updateAd.getStatusCode()).isEqualTo(HttpStatus.OK);
        Ad updatedAd = updateAd.getBody();
        assertThat(updatedAd).isNotNull();
        assertThat(updatedAd).usingRecursiveComparison().ignoringFields("image").isEqualTo(expectedAd);
    }

    @Test
    void updateAdsNegative_IfUserUnauthorizedTest() {
        AdEntity ad = listAd.get(faker.random().nextInt(listAd.size() - 1));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setDescription("Описание какого то товара");
        createOrUpdateAd.setPrice(faker.random().nextInt(0, 100));
        createOrUpdateAd.setTitle("Какой то товар");
        ad.setDescription(createOrUpdateAd.getDescription());
        ad.setPrice(createOrUpdateAd.getPrice());
        ad.setTitle(createOrUpdateAd.getTitle());
        HttpEntity<CreateOrUpdateAd> entityAd = new HttpEntity<>(createOrUpdateAd);
        ResponseEntity<Ad> updateAd = testRestTemplate.withBasicAuth(ad.getAuthor().getEmail(), "password1")
                .exchange(buildUrl("/ads/" + ad.getId()),
                        HttpMethod.PATCH,
                        entityAd,
                        Ad.class);
        assertThat(updateAd.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void updateAdsNegative_IfForbiddenTest() {
        AdEntity ad = listAd.get(faker.random().nextInt(listAd.size() - 1));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setDescription("Описание какого то товара");
        createOrUpdateAd.setPrice(faker.random().nextInt(0, 100));
        createOrUpdateAd.setTitle("Какой то товар");
        ad.setDescription(createOrUpdateAd.getDescription());
        ad.setPrice(createOrUpdateAd.getPrice());
        ad.setTitle(createOrUpdateAd.getTitle());
        HttpEntity<CreateOrUpdateAd> entityAd = new HttpEntity<>(createOrUpdateAd);
        ResponseEntity<Ad> updateAd = testRestTemplate.withBasicAuth(ad.getAuthor().getEmail(), "password")
                .exchange(buildUrl("/ads" + ad.getId()),
                        HttpMethod.PATCH,
                        entityAd,
                        Ad.class);
        assertThat(updateAd.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void updateAdsNegativeTest_IfAdNotFoundTest() {
        AdEntity ad = listAd.get(faker.random().nextInt(listAd.size() - 1));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setDescription("Описание какого то товара");
        createOrUpdateAd.setPrice(faker.random().nextInt(0, 100));
        createOrUpdateAd.setTitle("Какой то товар");
        ad.setDescription(createOrUpdateAd.getDescription());
        ad.setPrice(createOrUpdateAd.getPrice());
        ad.setTitle(createOrUpdateAd.getTitle());
        HttpEntity<CreateOrUpdateAd> entityAd = new HttpEntity<>(createOrUpdateAd);
        ResponseEntity<String> updateAd = testRestTemplate.withBasicAuth(ad.getAuthor().getEmail(), "password")
                .exchange(buildUrl("/ads/{id}"),
                        HttpMethod.PATCH,
                        entityAd,
                        String.class,
                        Map.of("id", -1));
        assertThat(updateAd.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAdsAuthorizedUserPositiveTest() {
        UserEntity user = listUser.get(faker.random().nextInt(listUser.size() - 1));
        List<Ad> ad = listAd.stream().filter(adEntity -> adEntity.getAuthor().getEmail().equals(user.getEmail())).map(adMapper::adEntityToAd).toList();

        Ads adsExpected = new Ads();
        adsExpected.setCount(ad.size());
        adsExpected.setResults(ad);

        ResponseEntity<Ads> adFormDb = testRestTemplate.withBasicAuth(user.getEmail(), "password")
                .getForEntity(buildUrl("/ads/me"), Ads.class);
        assertThat(adFormDb.getStatusCode()).isEqualTo(HttpStatus.OK);
        Ads get = adFormDb.getBody();
        assertThat(get).isNotNull();
        assertThat(get.getCount()).isEqualTo(adsExpected.getCount());
        assertThat(get.getResults()).usingRecursiveComparison().ignoringFields("image").isEqualTo(ad);
    }

    @Test
    void getAdsAuthorizedUserNegativeTest_IfUserUnauthorizedTest() {
        UserEntity user = listUser.get(faker.random().nextInt(listUser.size() - 1));
        List<Ad> ad = listAd.stream().filter(adEntity -> adEntity.getAuthor().getEmail().equals(user.getEmail())).map(adMapper::adEntityToAd).toList();

        Ads adsExpected = new Ads();
        adsExpected.setCount(ad.size());
        adsExpected.setResults(ad);

        ResponseEntity<Ads> adFormDb = testRestTemplate.withBasicAuth(user.getEmail(), "password1")
                .getForEntity(buildUrl("/ads/me"), Ads.class);
        assertThat(adFormDb.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }


    @Test
    void updateImagePositiveTest() throws InterruptedException {
        UserEntity user = userRepo.findAll().stream().toList().get(faker.random().nextInt(listUser.size() - 1));
        AdEntity adEntity = adRepo.findAll().stream().filter(adEntity1 -> adEntity1.getAuthor().getEmail().equals(user.getEmail())).findFirst().get();
        Path filPath = Paths.get(adImages, "3.jpg");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        FileSystemResource fileSystemResource = new FileSystemResource(filPath);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", fileSystemResource);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<List<String>> response = restTemplate.withBasicAuth(user.getEmail(), "password").exchange(
                buildUrl("/ads/{id}/image"),
                HttpMethod.PATCH,
                request,
                new ParameterizedTypeReference<>() {
                },
                Map.of("id", adEntity.getId())
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    void updateImageNegativeTest_IfUserUnauthorizedTest() throws InterruptedException {
        UserEntity user = listUser.get(faker.random().nextInt(listUser.size() - 1));
        Thread.sleep(100);
        AdEntity adEntity = listAd.stream().filter(adEntity1 -> adEntity1.getAuthor().equals(user)).findFirst().get();
        Path filPath = Paths.get(adImages, "3.jpg");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        FileSystemResource fileSystemResource = new FileSystemResource(filPath);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", fileSystemResource);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<List<String>> response = restTemplate.withBasicAuth(user.getEmail(), "password1").exchange(
                buildUrl("/ads/{id}/image"),
                HttpMethod.PATCH,
                request,
                new ParameterizedTypeReference<>() {
                },
                Map.of("id", adEntity.getId())
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void updateImageNegativeTest_IfForbiddenTest() {
        UserEntity user = listUser.get(faker.random().nextInt(listUser.size() - 1));
        AdEntity adEntity = listAd.stream().filter(adEntity1 -> adEntity1.getAuthor().getEmail().equals(user.getEmail())).findFirst().get();
        Path filPath = Paths.get(adImages, "3.jpg");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        FileSystemResource fileSystemResource = new FileSystemResource(filPath);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", fileSystemResource);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<List<String>> response = restTemplate.withBasicAuth(user.getEmail(), "password").exchange(
                buildUrl("/ads{id}/image"),
                HttpMethod.PATCH,
                request,
                new ParameterizedTypeReference<>() {
                },
                Map.of("id", adEntity.getId())
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void updateImageNegativeTest_IfNotFoundTest() throws InterruptedException {
        UserEntity user = listUser.get(faker.random().nextInt(listUser.size() - 1));
        Thread.sleep(100);
        AdEntity adEntity = listAd.stream().filter(adEntity1 -> adEntity1.getAuthor().equals(user)).findFirst().get();
        adEntity.setImage(null);
        Path filPath = Paths.get(adImages, "3.jpg");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        FileSystemResource fileSystemResource = new FileSystemResource(filPath);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", fileSystemResource);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.withBasicAuth(user.getEmail(), "password").exchange(
                buildUrl("/ads/{id}/image"),
                HttpMethod.PATCH,
                request,
                String.class,
                Map.of("id", -1)
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    void getAdImage() {
        UserEntity userEntity = listUser.get(faker.random().nextInt(0, listUser.size() - 1));
        Path filePath = Paths.get(adImages, "2.jpg");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Content_Type", MediaType.APPLICATION_JSON_VALUE);
        FileSystemResource fileSystemResource = new FileSystemResource(filePath);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", fileSystemResource);
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setDescription("Описание какого то товара");
        createOrUpdateAd.setPrice(faker.random().nextInt(0, 100));
        createOrUpdateAd.setTitle("Какой то товар");
        body.add("properties", createOrUpdateAd);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Ad> createAd = restTemplate.withBasicAuth(userEntity.getEmail(), "password")
                .postForEntity(buildUrl("/ads"),
                        requestEntity,
                        Ad.class);

        Ad adFormDb = createAd.getBody();
        Long id = adFormDb.getPk();
        ResponseEntity<String> response = testRestTemplate.withBasicAuth(userEntity.getEmail(), "password")
                .exchange(buildUrl("/ads/" + id + "/image"),
                        HttpMethod.GET,
                        null,
                        String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}