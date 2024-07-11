package ru.skypro.homework.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
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
public class FileManager {
    @Value("${path.to.userImages.folder}")
    private String userImagesFolder;

    @Value("${path.to.adImages.folder}")
    private String adImagesFolder;

    /**
     * Метод для записи файла с изображением на диск, а пути к этому файлу в базу данных.
     * @param username - email пользователя
     * @param image - файл с изображением
     * @return Path - объект пути к записанному файлу
     */
    public Path uploadUserPhoto(String username, MultipartFile image) {
        log.info("The uploadUserPhoto method of FileManager is called");
        try {
            String fileName = String.format(
                    "%s.%s",
                    username,
                    StringUtils.getFilenameExtension(image.getOriginalFilename())
            );
            byte[] data = image.getBytes();
            Path path = Paths.get(userImagesFolder, fileName);
            Files.write(path, data);
            return path;
        }catch (IOException e) {
            return null;
        }
    }

    /**
     * Метод для записи файла с изображением на диск, а пути к этому файлу в базу данных.
     * @param username - email пользователя, разместившего объявление
     * @param adTitle - название объявления
     * @param image - файл с изображением
     * @return Path - объект пути к записанному файлу
     */
    public Path uploadAdPhoto(String username, String adTitle, MultipartFile image) {
        log.info("The uploadAdPhoto method of FileManager is called");
        try {
            String fileName = String.format(
                    "%s_%s.%s",
                    username,
                    adTitle,
                    StringUtils.getFilenameExtension(image.getOriginalFilename())
            );
            byte[] data = image.getBytes();
            Path path = Paths.get(adImagesFolder, fileName);
            Files.write(path, data);
            return path;
        }catch (IOException e) {
            return null;
        }
    }

    /**
     * Метод для чтения файла по указанному пути
     * @param path - объект пути
     * @param response - ответ для записи содержимого файла
     */
    public void getImage(Path path, HttpServletResponse response) {
        log.info("The getImage method of FileManager is called");
        if(!Files.exists(path)){
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
