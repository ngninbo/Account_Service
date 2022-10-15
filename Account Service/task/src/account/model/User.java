package account.model;

import account.domain.Breach;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

@Entity
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
    private String role;

    public User() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @AssertFalse(message = "The password is in the hacker's database!")
    public boolean isBreached() {
        return this.password != null && Breach.getBreachedPasswords().contains(this.password);
    }

    @AssertTrue(message = "The password length must be at least 12 chars!")
    public boolean hasValideLength() {
        return this.password != null && this.password.length() >= 12;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getName(), user.getName()) && Objects.equals(getLastname(), user.getLastname()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getRole(), user.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLastname(), getEmail(), getPassword(), getRole());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
