package web_rize.user_subscription.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import web_rize.user_subscription.api.UserApi;
import web_rize.user_subscription.mapper.UserRestMapper;
import web_rize.user_subscription.model.UserDto;
import web_rize.user_subscription.model.UserRequestDto;
import web_rize.user_subscription.service.UserService;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;
    private final UserRestMapper userRestMapper;

    @Override
    public ResponseEntity<List<UserDto>> findAllUsers() {
        var users = userService.findAll();
        return ResponseEntity.ok(userRestMapper.toDto(users));
    }

    @Override
    public ResponseEntity<UserDto> findUserById(UUID id) {
        var user = userService.findById(id);
        return ResponseEntity.ok(userRestMapper.toDto(user));
    }

    @Override
    public ResponseEntity<Void> createUser(UserRequestDto request) {
        userService.save(userRestMapper.toEntity(request));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> updateUser(UUID id, UserRequestDto request) {
        userService.update(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
