package br.com.thiagoodev.cmsapi.domain.models;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class UserTests {
    private Faker faker;
    private String rawPassword;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        rawPassword = faker.internet().password(
            8,
            12,
            true,
            true,
            true
        );
    }

    @Test
    @DisplayName("Should create user with valid data using factory method")
    void shouldCreateUserWithValidData() {
        String name = faker.name().fullName();
        String username = faker.name().username();
        String email = faker.internet().emailAddress();
        PermissionLevel permission = PermissionLevel.VIEWER;

        String salt = BCrypt.gensalt();
        String passwordHash = BCrypt.hashpw(rawPassword, salt);

        User user = User.create(
            name,
            username,
            email,
            passwordHash,
            permission
        );

        assertNotNull(user);
        assertNotNull(user.getUuid());
        assertFalse(user.getUuid().isBlank());
        assertEquals(name, user.getName());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertFalse(user.getPasswordHash().isBlank());
        assertFalse(user.isEmailIsConfirmed());
        assertEquals(permission, user.getPermissionLevel());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertNull(user.getDeletedAt());
        assertEquals(user.getCreatedAt(), user.getUpdatedAt());
    }
}
