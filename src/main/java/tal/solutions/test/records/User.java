package tal.solutions.test.records;

import java.util.Objects;

public record User(Long id, String login, String firstName, String lastName) {

    public User(Long id, String login) {
        this(id, login, null, null);
    }

    public User(Long id, String login, String firstName, String lastName) {
        this.id = Objects.requireNonNull(id);
        this.login = Objects.requireNonNull(login);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
