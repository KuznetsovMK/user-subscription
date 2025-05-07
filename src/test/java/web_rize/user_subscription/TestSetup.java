package web_rize.user_subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TestSetup {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional
    public void deleteAll() {
        jdbcTemplate.update("""
                DO
                $$
                    BEGIN
                        DELETE FROM users_subscriptions.user_subscription;
                        DELETE FROM users_subscriptions.user;
                        DELETE FROM users_subscriptions.subscription;
                    END
                $$
                """, Map.of());
    }
}
