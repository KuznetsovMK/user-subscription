package web_rize.user_subscription.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = {"id", "title"})
@ToString(exclude = "users")
@Entity
@Table(name = "subscription", schema = "users_subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @ManyToMany(mappedBy = "subscriptions", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<User> users = new ArrayList<>();
}
