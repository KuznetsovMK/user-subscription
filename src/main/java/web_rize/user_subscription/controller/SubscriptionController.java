package web_rize.user_subscription.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import web_rize.user_subscription.api.SubscriptionsApi;
import web_rize.user_subscription.mapper.SubscriptionRestMapper;
import web_rize.user_subscription.model.SubscriptionDto;
import web_rize.user_subscription.service.SubscriptionService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SubscriptionController implements SubscriptionsApi {
    private final SubscriptionService subscriptionService;
    private final SubscriptionRestMapper subscriptionRestMapper;

    @Override
    public ResponseEntity<List<SubscriptionDto>> topSubscriptions(Integer limit) {
        var top = subscriptionService.findTop(limit);
        return ResponseEntity.ok(subscriptionRestMapper.toDto(top));
    }
}
