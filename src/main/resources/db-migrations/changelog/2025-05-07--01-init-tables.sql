--liquibase formatted sql

--changeset Kuznetsov.Mikhail:1


CREATE SCHEMA IF NOT EXISTS users_subscriptions;

CREATE TABLE IF NOT EXISTS users_subscriptions.user
(
    id        uuid NOT NULL,
    full_name text NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users_subscriptions.subscription
(
    id    bigint NOT NULL,
    title text   NOT NULL,
    CONSTRAINT subscription_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users_subscriptions.user_subscription
(
    user_id         uuid   NOT NULL,
    subscription_id bigint NOT NULL,
    CONSTRAINT user_subscription_pk PRIMARY KEY (user_id, subscription_id),
    CONSTRAINT fk_user_subscription_user_id FOREIGN KEY (user_id) REFERENCES users_subscriptions.user (id),
    CONSTRAINT fk_user_subscription_subscription_id FOREIGN KEY (subscription_id) REFERENCES users_subscriptions.subscription (id)
);

INSERT INTO users_subscriptions.subscription (id, title)
VALUES (1, 'YouTube Premium')
ON CONFLICT DO NOTHING;

INSERT INTO users_subscriptions.subscription (id, title)
VALUES (2, 'VK Музыка')
ON CONFLICT DO NOTHING;

INSERT INTO users_subscriptions.subscription (id, title)
VALUES (3, 'Яндекс.Плюс')
ON CONFLICT DO NOTHING;

INSERT INTO users_subscriptions.subscription (id, title)
VALUES (4, 'Netflix')
ON CONFLICT DO NOTHING;