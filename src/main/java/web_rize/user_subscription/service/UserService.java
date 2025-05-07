package web_rize.user_subscription.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import web_rize.user_subscription.entity.Subscription;
import web_rize.user_subscription.entity.User;
import web_rize.user_subscription.exception.NotFoundException;
import web_rize.user_subscription.mapper.UserMapper;
import web_rize.user_subscription.model.UserRequestDto;
import web_rize.user_subscription.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<User> findAll() {
        return userRepository.findAllByOrderByFullName();
    }

    public User findById(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Пользователь id: {} не найден", id);
                    return new NotFoundException("User not found");
                });
    }

    public List<Subscription> findUserSubscriptions(UUID id) {
        return findById(id)
                .getSubscriptions()
                .stream()
                .sorted()
                .toList();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void update(UUID id, UserRequestDto request) {
        var exist = findById(id);
        var updated = userMapper.updateEntity(exist, request);
        userRepository.save(updated);
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
