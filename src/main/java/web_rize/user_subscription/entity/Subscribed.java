package web_rize.user_subscription.entity;

public interface Subscribed {
    void subscribe(Subscription subscription);

    void unsubscribe(Subscription subscription);
}
