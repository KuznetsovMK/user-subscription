package web_rize.user_subscription.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import web_rize.user_subscription.model.SubscriptionRequestDto;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSubscriptionService {
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public void addSubscription(UUID userId, SubscriptionRequestDto request) {
        var user = userService.findById(userId);
        var sub = subscriptionService.findByTitle(request.getTitle());

        user.subscribe(sub);
        userService.save(user);
    }

    public void removeSubscription(UUID userId, Long subId) {
        var user = userService.findById(userId);
        var sub = subscriptionService
                .findById(subId)
                .orElseThrow(() -> new RuntimeException("Sub not found"));

        user.unsubscribe(sub);
        userService.save(user);
    }
}
