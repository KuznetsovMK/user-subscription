package web_rize.user_subscription.controller;


import io.restassured.RestAssured;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import web_rize.user_subscription.TestSetup;
import web_rize.user_subscription.entity.Subscription;
import web_rize.user_subscription.entity.User;

import java.util.*;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final BeanPropertyRowMapper<User> ROW_MAPPER = new BeanPropertyRowMapper<>(User.class);

    @LocalServerPort
    private int localServerPort;
    @Autowired
    private TestSetup testSetup;
    @Autowired
    private JpaTransactionManager jpaTransactionManager;
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        testSetup.deleteAll();

        RestAssured.port = localServerPort;
        RestAssured.baseURI = "http://localhost";
    }

    @SneakyThrows
    @Test
    void findAllUsers() {
        dbPrepare();

        var result =
                given()
                        .log().all()
                        .when().get("/api/user/find-all")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        JSONAssert.assertEquals("""
                [
                    {
                        "id": "00000000-0000-0000-0000-000000000001",
                        "fullName": "Владимиров Мирон Даниилович",
                        "subscriptions": []
                    }
                ]
                """, result, false);
    }

    @SneakyThrows
    @Test
    void findUserById() {
        dbPrepare();

        var result =
                given()
                        .log().all()
                        .pathParam("id", "00000000-0000-0000-0000-000000000001")
                        .when().get("/api/user/{id}")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        JSONAssert.assertEquals("""
                {
                    "id": "00000000-0000-0000-0000-000000000001",
                    "fullName": "Владимиров Мирон Даниилович",
                    "subscriptions": []
                }
                """, result, false);
    }

    @SneakyThrows
    @Test
    void createUser() {
        var request = """
                {
                    "fullName": "Белов Али Александрович"
                }
                """;

        given()
                .contentType("application/json")
                .log().all()
                .body(request)
                .when().post("/api/user/create")
                .then()
                .log().all()
                .statusCode(201);

        var user = jdbcTemplate.query(
                        """
                                SELECT *
                                FROM users_subscriptions.user;
                                """,
                        Map.of(),
                        ROW_MAPPER
                )
                .stream().findFirst().orElseThrow();

        Assertions.assertEquals(
                "Белов Али Александрович",
                user.getFullName()
        );
    }

    @Test
    void updateUser() {
        dbPrepare();

        var request = """
                {
                    "fullName": "Белов Али Александрович"
                }
                """;

        given()
                .contentType("application/json")
                .log().all()
                .pathParam("id", "00000000-0000-0000-0000-000000000001")
                .body(request)
                .when().put("/api/user/update/{id}")
                .then()
                .log().all()
                .statusCode(204);

        var user = jdbcTemplate.query(
                        """
                                SELECT *
                                FROM users_subscriptions.user;
                                """,
                        Map.of(),
                        ROW_MAPPER
                )
                .stream().findFirst().orElseThrow();

        Assertions.assertEquals(
                "Белов Али Александрович",
                user.getFullName()
        );
    }

    @Test
    void deleteUser() {
        dbPrepare();

        given()
                .contentType("application/json")
                .log().all()
                .pathParam("id", "00000000-0000-0000-0000-000000000001")
                .when().delete("/api/user/delete/{id}")
                .then()
                .log().all()
                .statusCode(204);

        var user = jdbcTemplate.query(
                        """
                                SELECT *
                                FROM users_subscriptions.user;
                                """,
                        Map.of(),
                        ROW_MAPPER
                )
                .stream().findFirst().orElse(null);

        Assertions.assertNull(user);
    }

    @SneakyThrows
    @Test
    void findUserSubscriptions() {
        dbPrepareExtended();

        var result =
                given()
                        .log().all()
                        .pathParam("id", "00000000-0000-0000-0000-000000000001")
                        .when().get("/api/user/{id}/find-subscriptions")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        JSONAssert.assertEquals("""
                [
                    {
                        "id": 0,
                        "title": "YouTube Premium"
                    }
                ]
                """, result, false);
    }

    private void dbPrepare() {
        execute(userSql(), source(user()));
    }

    private void dbPrepareExtended() {
        var user = user();
        var sub = sub();

        execute(userSql(), source(user));
        execute(subSql(), source(sub));

        Map<String, Object> params =
                Map.of(
                        "userId", user.getId(),
                        "subscriptionId", sub.get(0).getId()
                );

        execute(userSubSql(), source(params));
    }

    private User user() {
        var user = new User();

        user.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        user.setFullName("Владимиров Мирон Даниилович");

        return user;
    }

    private List<Subscription> sub() {
        return subs(List.of("YouTube Premium", "VK Музыка"));
    }

    private List<Subscription> subs(List<String> titles) {
        var result = new ArrayList<Subscription>();

        for (int i = 0; i < titles.size(); i++) {
            var subscription = new Subscription();

            subscription.setId((long) i);
            subscription.setTitle(titles.get(i));

            result.add(subscription);
        }

        return result;
    }

    private String userSql() {
        return """
                INSERT INTO users_subscriptions.user (id, full_name)
                VALUES (:id, :fullName)
                """;
    }

    private String subSql() {
        return """
                INSERT INTO users_subscriptions.subscription (id, title)
                VALUES (:id, :title)
                """;
    }

    private String userSubSql() {
        return """
                INSERT INTO users_subscriptions.user_subscription (user_id, subscription_id)
                VALUES (:userId, :subscriptionId)
                """;
    }

    private SqlParameterSource source(User user) {
        return Arrays
                .stream(SqlParameterSourceUtils.createBatch(user))
                .findFirst()
                .orElse(new EmptySqlParameterSource());
    }

    private SqlParameterSource[] source(List<Subscription> subscriptions) {
        return SqlParameterSourceUtils.createBatch(subscriptions);
    }

    private SqlParameterSource source(Map<String, Object> map) {
        return Arrays
                .stream(SqlParameterSourceUtils.createBatch(map))
                .findFirst()
                .orElse(new EmptySqlParameterSource());
    }

    private void execute(String sql, SqlParameterSource source) {
        var template = new TransactionTemplate(jpaTransactionManager);
        template.execute(e -> {
            jdbcTemplate.update(sql, source);
            return null;
        });
    }

    private void execute(String sql, SqlParameterSource[] sources) {
        var template = new TransactionTemplate(jpaTransactionManager);
        template.execute(e -> {
            jdbcTemplate.batchUpdate(sql, sources);
            return null;
        });
    }
}