package ru.skypro.homework.utils;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.WebSecurityConfig;
import ru.skypro.homework.exceptions.ImageNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
@Component
@AllArgsConstructor
public class FileManager {
    private final WebSecurityConfig webSecurityConfig;

    /**
     * Метод для создания папок для хранения изображений пользователей и объявлений
     * если они не существуют
     * @throws IOException - исключение при обращении с файлами
     */
    @PostConstruct
    public void prepare() throws IOException {
        Path userImagesPath = Paths.get(webSecurityConfig.getUserImagesFolder());
        if (Files.notExists(userImagesPath)) {
            Files.createDirectories(userImagesPath);
        }
        Path adImagesPath = Paths.get(webSecurityConfig.getAdImagesFolder());
        if (Files.notExists(adImagesPath)) {
            Files.createDirectories(adImagesPath);
        }
    }


    /**
     * Метод для записи файла с изображением на диск, а пути к этому файлу в базу данных.
     * @param uuid - uuid
     * @param image - файл с изображением
     * @return Path - объект пути к записанному файлу
     */
    public Path uploadUserPhoto(String uuid, MultipartFile image) {
        log.info("The uploadUserPhoto method of FileManager is called");
        try {
            String fileName = String.format(
                    "%s.%s",
                    uuid,
                    StringUtils.getFilenameExtension(image.getOriginalFilename())
            );
            byte[] data = image.getBytes();
            Path path = Paths.get(webSecurityConfig.getUserImagesFolder(), fileName);
            Files.write(path, data);
            return path;
        }catch (IOException e) {
            return null;
        }
    }

    /**
     * Метод для записи файла с изображением на диск, а пути к этому файлу в базу данных.
     * @param uuid - uuid
     * @param image - файл с изображением
     * @return Path - объект пути к записанному файлу
     */
    public Path uploadAdPhoto(String uuid, MultipartFile image) {
        log.info("The uploadAdPhoto method of FileManager is called");
        try {
            String fileName = String.format(
                    "%s.%s",
                    uuid,
                    StringUtils.getFilenameExtension(image.getOriginalFilename())
            );
            byte[] data = image.getBytes();
            Path path = Paths.get(webSecurityConfig.getAdImagesFolder(), fileName);
            Files.write(path, data);
            return path;
        }catch (IOException e) {
            return null;
        }
    }

    /**
     * Метод для чтения файла по указанному пути
     * @param path - путь к файлу
     * @param response - ответ для записи содержимого файла
     */
    public void getImage(Path path, HttpServletResponse response) {
        log.info("The getImage method of FileManager is called");
        if(Files.notExists(path)){
            log.error("The image file was not found!");
            throw new ImageNotFoundException("Файл с изображением не найден!");
        }
        try(InputStream is = Files.newInputStream(path);
            OutputStream os = response.getOutputStream()){
            response.setContentType(Files.probeContentType(path));
            response.setContentLength(Files.readAllBytes(path).length);
            is.transferTo(os);
        } catch (IOException e) {
            throw new RuntimeException(e.getCause() + e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
    }
}
