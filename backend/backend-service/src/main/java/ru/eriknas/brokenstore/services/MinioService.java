package ru.eriknas.brokenstore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.eriknas.brokenstore.components.MinioComponent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@Service
public class MinioService {

    @Autowired
    private MinioComponent minioComponent;

    public MinioService() {
    }

    public String uploadFileToMinIO(MultipartFile picture, String article) throws Exception {
        if (picture == null) {
            return article;
        }
        if (!(MediaType.IMAGE_JPEG_VALUE.equals(picture.getContentType()) || MediaType.IMAGE_PNG_VALUE.equals(picture.getContentType()))) {
            throw new Exception(
                    String.format("Некорректный файл. Допустимые типы файла %s, %s", MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE)
            );
        }
        if (article == null) {
            throw new Exception("Не указан артикул товара");
        }

        InputStream in = new ByteArrayInputStream(picture.getBytes());
        minioComponent.putObject(article, in);
        return article;
    }

    public String downloadFile(String fileName) throws Exception {
        return minioComponent.getObject(fileName);
    }

    public void removeFile(String fileName) throws Exception {
        if (fileName == null) {
            return;
        }
        minioComponent.removeObject(fileName);
    }
}