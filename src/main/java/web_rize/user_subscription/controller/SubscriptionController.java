package web_rize.user_subscription.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionController.class);
    private final SubscriptionService subscriptionService;
    private final SubscriptionRestMapper subscriptionRestMapper;

    @Override
    public ResponseEntity<List<SubscriptionDto>> topSubscriptions(Integer limit) {
        LOGGER.info("Поиск топ {} самых популярных подписок", limit);
        var top = subscriptionService.findTop(limit);
        return ResponseEntity.ok(subscriptionRestMapper.toDto(top));
    }
}
