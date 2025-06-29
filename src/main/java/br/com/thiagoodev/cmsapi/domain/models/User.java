package br.com.thiagoodev.cmsapi.domain.models;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class User {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
    );

    private final String uuid;
    private String name;
    private String username;
    private String email;
    private String passwordHash;
    private boolean emailIsConfirmed;
    private PermissionLevel permissionLevel;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public User(
        String uuid,
        String name,
        String username,
        String email,
        String passwordHash,
        boolean emailIsConfirmed,
        PermissionLevel permissionLevel,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
    ) {
        if (uuid == null || uuid.isBlank()) throw new IllegalArgumentException("UUID cannot be null or blank.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be null or blank.");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username cannot be null or blank.");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email cannot be null or blank.");
        if (!EMAIL_PATTERN.matcher(email).matches()) throw new IllegalArgumentException("Invalid email format.");
        if (passwordHash == null || passwordHash.isBlank()) throw new IllegalArgumentException("Password hash cannot be null or blank.");
        if (permissionLevel == null) throw new IllegalArgumentException("Permission level cannot be null.");
        if (createdAt == null) throw new IllegalArgumentException("Created at cannot be null.");

        this.uuid = uuid;
        this.name = name;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.emailIsConfirmed = emailIsConfirmed;
        this.permissionLevel = permissionLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static User create(
        String name,
        String username,
        String email,
        String passwordHash,
        PermissionLevel initialPermissionLevel
    ) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be null or blank.");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username cannot be null or blank.");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email cannot be null or blank.");
        if (!EMAIL_PATTERN.matcher(email).matches()) throw new IllegalArgumentException("Invalid email format.");
        if (passwordHash == null || passwordHash.isBlank()) throw new IllegalArgumentException("Password cannot be null or blank.");
        if (initialPermissionLevel == null) throw new IllegalArgumentException("Initial permission level cannot be null.");

        String newUuid = UUID.randomUUID().toString();
        Instant now = Instant.now();

        return new User(
            newUuid,
            name,
            username,
            email,
            passwordHash,
            false,
            initialPermissionLevel,
            now,
            now,
            null
        );
    }

    public String getUuid() { return uuid; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public boolean isEmailIsConfirmed() { return emailIsConfirmed; }
    public PermissionLevel getPermissionLevel() { return permissionLevel; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Instant getDeletedAt() { return deletedAt; }

    public void changeName(String newName) {
        if (newName == null || newName.isBlank()) throw new IllegalArgumentException("Name cannot be null or blank.");
        this.name = newName;
        this.updatedAt = Instant.now();
    }

    public void changeUsername(String newUserName) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username cannot be null or blank.");
        this.username = newUserName;
        this.updatedAt = Instant.now();
    }

    public void changeEmail(String newEmail) {
        if (newEmail == null || newEmail.isBlank()) throw new IllegalArgumentException("Email cannot be null or blank.");
        if (!EMAIL_PATTERN.matcher(newEmail).matches()) throw new IllegalArgumentException("Invalid email format.");
        this.email = newEmail;
        this.emailIsConfirmed = false;
        this.updatedAt = Instant.now();
    }

    public void updatePassword(String newPassword) {
        if (newPassword == null || newPassword.isBlank()) throw new IllegalArgumentException("Password cannot be null or blank.");
        this.passwordHash = newPassword;
        this.updatedAt = Instant.now();
    }

    public void confirmEmail() {
        if (this.emailIsConfirmed) throw new IllegalStateException("Email is already confirmed.");
        this.emailIsConfirmed = true;
        this.updatedAt = Instant.now();
    }

    public void changePermissionLevel(PermissionLevel newPermissionLevel) {
        if (newPermissionLevel == null) throw new IllegalArgumentException("Permission level cannot be null.");
        this.permissionLevel = newPermissionLevel;
        this.updatedAt = Instant.now();
    }

    public void markAsDeleted() {
        if (this.deletedAt != null) throw new IllegalStateException("User is already marked as deleted.");
        this.deletedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void restore() {
        if (this.deletedAt == null) throw new IllegalStateException("User is not marked as deleted.");
        this.deletedAt = null;
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(uuid, user.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", emailIsConfirmed=" + emailIsConfirmed +
                ", permissionLevel=" + permissionLevel +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
}