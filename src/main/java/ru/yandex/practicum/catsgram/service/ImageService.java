package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.exception.ImageFileException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Image;
import ru.yandex.practicum.catsgram.model.ImageData;
import ru.yandex.practicum.catsgram.model.Post;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final PostService postService;
    private final Map<Long, Image> images = new HashMap<>();
    private final String imageDirectory = "/home/tilteteduser/IdeaProjects/Catsgram/images";


    public ImageData getImageData(long imageId) {
        if (!images.containsKey(imageId)) {
            throw new NotFoundException("Фотографии с id = " + imageId + " не найдено");
        }
        Image image = images.get(imageId);

        byte[] data = loadFile(image);

        return new ImageData(data, image.getOriginalFileName());
    }

    private byte[] loadFile(Image image) {
        Path path = Paths.get(image.getFilePath());

        if (Files.exists(path)) {
            try {
                return Files.readAllBytes(path);
            } catch (IOException e) {
                throw new ImageFileException("Ошибка при чтение файла. Id = " + image.getId()
                        + ", name = " + image.getOriginalFileName());
            }
        } else {
            throw new ImageFileException("Файл не найден. Id = " + image.getId()
                    + ", name = " + image.getOriginalFileName());

        }
    }

    public List<Image> getPostImages(long postId) {
        return images.values().stream()
                .filter(image -> image.getPostId() == postId)
                .collect(Collectors.toList());
    }

    public List<Image> saveImages(long postId, List<MultipartFile> files) {
        return files.stream()
                .map(file -> saveImage(postId, file))
                .collect(Collectors.toList());
    }

    public Image saveImage(long postId, MultipartFile file) {
        Post post = postService.findById(postId);

        Path filePath = saveFile(file, post);


        long imageId = getNextId();
        Image image = new Image();

        image.setId(imageId);
        image.setFilePath(filePath.toString());
        image.setPostId(postId);
        image.setOriginalFileName(file.getOriginalFilename());
        images.put(imageId, image);

        return image;
    }

    public Path saveFile(MultipartFile file, Post post) {

        try {
            String uniqueFileName = String.format("%d.%s", Instant.now().toEpochMilli(),
                    StringUtils.getFilenameExtension(file.getOriginalFilename()));
            Path uploadPath = Paths.get(imageDirectory, String.valueOf(post.getAuthorId()), post.getId().toString());
            Path filePath = uploadPath.resolve(uniqueFileName);
            if (!Files.exists(filePath)) {
                Files.createDirectories(uploadPath);
            }

            file.transferTo(filePath);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long getNextId() {
        long currentMaxId = images.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
