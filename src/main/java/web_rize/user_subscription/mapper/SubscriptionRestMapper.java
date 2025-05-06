package web_rize.user_subscription.mapper;

import org.mapstruct.Mapper;
import web_rize.user_subscription.entity.Subscription;
import web_rize.user_subscription.model.SubscriptionDto;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionRestMapper {

    List<SubscriptionDto> toDto(Collection<Subscription> subscriptions);
}
