package tal.solutions.test.records;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void user_record_immutable() {
        // When
        final User user = new User(1L, "luongth", "Thuan", "Luong");

        // Then
        assertThat(user.id()).isEqualTo(1L);
        assertThat(user.login()).isEqualTo("luongth");
        assertThat(user.firstName()).isEqualTo("Thuan");
        assertThat(user.lastName()).isEqualTo("Luong");
    }

    @Test
    public void user_record_equals() {
        // When
        final User user1 = new User(1L, "luongth", "Thuan", "Luong");
        final User user2 = new User(1L, "luongth", "Thuan", "Luong");
        final User user3 = new User(1L, "luongth");

        // Then
        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);
        assertThat(user2).isNotEqualTo(user3);
    }
}
