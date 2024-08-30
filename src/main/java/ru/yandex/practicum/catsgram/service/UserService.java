package ru.yandex.practicum.catsgram.service;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (checkUserDuplicate(user)) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (!oldUser.equals(newUser)) {
                if (checkUserDuplicate(newUser)) {
                    throw new ConditionsNotMetException("Этот имейл уже используется");
                }
            }
            if (newUser.getPassword() != null || newUser.getUsername() != null || newUser.getEmail() != null) {
                users.put(newUser.getId(), newUser);
                return newUser;
            }

        }

        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    public Optional<User> findById(Long id) {
        return users.entrySet().stream()
                .filter(entry -> entry.getKey().equals(id))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    private boolean checkUserDuplicate(User checkUser) {
        List<User> duplicates = users.values()
                .stream()
                .filter(user -> user.equals(checkUser))
                .toList();
        return !duplicates.isEmpty();
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
