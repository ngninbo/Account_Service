package account.model.user;

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
}
