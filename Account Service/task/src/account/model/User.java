package account.model;

import account.util.Blacklist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "sequence", sequenceName = "UserSeq")
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

    @AssertFalse(message = "The password is in the hacker's database!")
    public boolean isBreached() {
        return this.password != null && Blacklist.contains(this.password);
    }

    @AssertTrue(message = "The password length must be at least 12 chars!")
    public boolean hasValideLength() {
        return this.password != null && this.password.length() >= 12;
    }

    public boolean isAdmin() {
        return this.groups.stream().map(Group::getRole).collect(Collectors.toList()).contains(Role.ROLE_ADMINISTRATOR);
    }

    public boolean hasRole(Role role) {
        return groups.stream().map(Group::getRole).anyMatch(role1 -> role1.equals(role));
    }

    public boolean isBusinessUser() {
        return groups.stream().map(Group::getRole).collect(Collectors.toList()).containsAll(List.of(Role.ROLE_USER, Role.ROLE_ACCOUNTANT));
    }
}
