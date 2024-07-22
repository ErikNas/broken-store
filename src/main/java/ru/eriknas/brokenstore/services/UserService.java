package ru.eriknas.brokenstore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.eriknas.brokenstore.exception.UserNotFoundException;
import ru.eriknas.brokenstore.model.User;
import ru.eriknas.brokenstore.repository.UserRepository;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        // Проверка входных данных пользователя
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("Некорректные данные пользователя");
        }
        // Сохранение пользователя в базе данных
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException("Пользователь с username: " + username + " не найден"));
    }
}
