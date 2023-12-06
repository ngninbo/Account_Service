package account.model.user;

import account.util.Blacklist;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "sequence", sequenceName = "UserSeq")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence")
    @Column(name = "user_id")
    private Long id;

    @NotEmpty(message = "The user name must not be empty")
    private String name;

    @NotEmpty(message = "last name must not be empty")
    private String lastname;

    @NotEmpty(message = "Email must not be empty")
    @Pattern(regexp = ".*@acme\\.com", message = "Email from given domain not allowed")
    private String email;

    @NotNull
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Group> groups = new LinkedHashSet<>();

    private boolean enabled = true;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;

    @Column(name = "failed_attempt")
    private int failedAttempt;

    @AssertFalse(message = "The password is in the hacker's database!")
    public boolean isBreached() {
        return this.password != null && Blacklist.contains(this.password);
    }

    @AssertTrue(message = "The password length must be at least 12 chars!")
    public boolean hasValideLength() {
        return this.password != null && this.password.length() >= 12;
    }

    public boolean isAdmin() {
        return this.groups.stream().map(Group::getRole).collect(Collectors.toList()).contains(Role.ADMINISTRATOR);
    }

    public boolean hasRole(Role role) {
        return groups.stream().map(Group::getRole).anyMatch(r -> r.equals(role));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
