package web_rize.user_subscription.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import web_rize.user_subscription.api.UserApi;
import web_rize.user_subscription.mapper.SubscriptionRestMapper;
import web_rize.user_subscription.mapper.UserRestMapper;
import web_rize.user_subscription.model.SubscriptionDto;
import web_rize.user_subscription.model.SubscriptionRequestDto;
import web_rize.user_subscription.model.UserDto;
import web_rize.user_subscription.model.UserRequestDto;
import web_rize.user_subscription.service.UserService;
import web_rize.user_subscription.service.UserSubscriptionService;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserController implements UserApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserSubscriptionService userSubscriptionService;
    private final UserRestMapper userRestMapper;
    private final SubscriptionRestMapper subscriptionRestMapper;

    @Override
    public ResponseEntity<List<UserDto>> findAllUsers() {
        LOGGER.info("Поиск всех пользователей");
        var users = userService.findAll();
        return ResponseEntity.ok(userRestMapper.toDto(users));
    }

    @Override
    public ResponseEntity<UserDto> findUserById(UUID id) {
        LOGGER.info("Поиск пользователя по id");
        var user = userService.findById(id);
        return ResponseEntity.ok(userRestMapper.toDto(user));
    }

    @Override
    public ResponseEntity<Void> createUser(UserRequestDto request) {
        LOGGER.info("Создание нового пользователя");
        userService.save(userRestMapper.toEntity(request));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> updateUser(UUID id, UserRequestDto request) {
        LOGGER.info("Старт обновление данных о пользователе");
        userService.update(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        LOGGER.info("Старт удаление пользователя");
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<SubscriptionDto>> findUserSubscriptions(UUID id) {
        LOGGER.info("Поиск всех подписок пользователя");
        var subs = userService.findUserSubscriptions(id);
        return ResponseEntity.ok(subscriptionRestMapper.toDto(subs));
    }

    @Override
    public ResponseEntity<Void> addSubscription(UUID userId, SubscriptionRequestDto request) {
        LOGGER.info("Старт добавление пользователю подписки");
        userSubscriptionService.addSubscription(userId, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> removeSubscription(UUID userId, Long subId) {
        LOGGER.info("Старт отмены подписки пользователя");
        userSubscriptionService.removeSubscription(userId, subId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
