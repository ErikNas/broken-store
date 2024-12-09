package ru.eriknas.brokenstore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.eriknas.brokenstore.dto.users.UserDTO;
import ru.eriknas.brokenstore.mappers.UsersMapper;
import ru.eriknas.brokenstore.models.entities.UsersEntity;
import ru.eriknas.brokenstore.repository.UsersRepository;

import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UsersEntity addUsers(UserDTO userDTO) {
        return usersRepository.save(UsersMapper.toEntity(userDTO));
    }

    public Optional<UsersEntity> getUsersById(int id) {
        return usersRepository.findById(id);
    }

    public Page<UsersEntity> getAllUsers(int page, int size) {
        return usersRepository.findAll(PageRequest.of(page, size));
    }

    public void deleteById(int id) {
        usersRepository.deleteById(id);
    }
}
