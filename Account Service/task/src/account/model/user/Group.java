package account.model.user;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "group_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, updatable = false)
    private Role role;

    public Group(Role role) {
        this.role = role;
    }
}
