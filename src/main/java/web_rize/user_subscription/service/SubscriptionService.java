package web_rize.user_subscription.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import web_rize.user_subscription.entity.Subscription;
import web_rize.user_subscription.entity.User;
import web_rize.user_subscription.repository.SubscriptionRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionService.class);
    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;

    public Subscription findByTitle(String title) {
        return subscriptionRepository.findByTitle(title);
    }

    public Optional<Subscription> findById(Long id) {
        return subscriptionRepository.findById(id);
    }

    public List<Subscription> findTop(Integer limit) {
        var users = userService.findAll();
        return counting(users, limit);
    }

    private List<Subscription> counting(List<User> users, Integer limit) {
        LOGGER.info("Начинается подсчёт самых популярных подписок");
        return users
                .stream()
                .flatMap(user -> user.getSubscriptions().stream())
                .collect(Collectors.groupingBy(Function.identity(),
                        Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Subscription, Long>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }
}
