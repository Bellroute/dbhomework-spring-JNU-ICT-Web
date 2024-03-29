package codesquad.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column
    private String email;

    public void update(User updatedUser) {
        this.name = updatedUser.getName();
        this.email = updatedUser.getEmail();
    }

    public boolean isCorrectPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isSameUser(Long id) {
        return this.id.equals(id);
    }
}
