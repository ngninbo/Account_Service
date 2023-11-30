package account.model.user;

import java.util.stream.Stream;

public enum Role {

    ROLE_ACCOUNTANT("ACCOUNTANT"),
    ROLE_ADMINISTRATOR("ADMINISTRATOR"),
    ROLE_AUDITOR("AUDITOR"),
    ROLE_USER("USER");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static String[] getChangePassRoles() {
        return Stream.of(ROLE_USER, ROLE_ACCOUNTANT, ROLE_ADMINISTRATOR).map(Role::getDescription).toArray(String[]::new);
    }

    public static String[] getPaymentRoles() {
        return Stream.of(ROLE_USER, ROLE_ACCOUNTANT).map(Role::getDescription).toArray(String[]::new);
    }
}
