package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.dal.PostRepository;
import ru.yandex.practicum.catsgram.dto.post.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.post.PostDto;
import ru.yandex.practicum.catsgram.dto.post.UpdatePostRequest;
import ru.yandex.practicum.catsgram.dto.user.UserDto;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.mapper.PostMapper;
import ru.yandex.practicum.catsgram.mapper.UserMapper;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(UserService userService, PostRepository postRepository) {
        this.postRepository = postRepository;
        this.userService = userService;
    }
/*
    public Collection<Post> findAll(int from, int size, SortOrder sort) {
        Comparator<Post> comparator = Comparator.comparing(Post::getPostDate);
        if (sort == SortOrder.DESCENDING) {
            comparator = comparator.reversed();
        }
        return posts.values().stream()
                .sorted(comparator)
                .skip(from)
                .limit(size)
                .toList();
    }
*/
    public PostDto createPost(NewPostRequest request) {
        if (request.getAuthorId() == null) {
            throw new ConditionsNotMetException("Айди автор не должно быть пустым");
        }

        UserDto dto = userService.getUserById(request.getAuthorId());
        Post post = PostMapper.mapToPost(request);
        post = postRepository.save(post);
        return PostMapper.mapToPostDto(post);
    }

    public List<PostDto> getPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostMapper::mapToPostDto)
                .collect(Collectors.toList());
    }

    public PostDto getPostById(Long id) {
        return postRepository.findById(id)
                .map(PostMapper::mapToPostDto)
                .orElseThrow(() -> new NotFoundException("Пост не найден с ID: " + id));
    }

    public PostDto update(long postId, UpdatePostRequest request) {
        Post updatedPost = postRepository.findById(postId)
                .map(post -> PostMapper.updatePostFields(spost, request))
                .orElseThrow(() -> new NotFoundException("Пост не найден"));
        updatedPost = postRepository.update(updatedPost);
        return PostMapper.mapToPostDto(updatedPost);
    }

}