package account.domain.user;

public class PasswordChangeResponseBuilder {

    public static final String DEFAULT_STATUS = "The password has been updated successfully";

    private String email;
    private String status;

    {
        this.status = DEFAULT_STATUS;
    }

    private PasswordChangeResponseBuilder() {
    }

    public static PasswordChangeResponseBuilder init() {
        return new PasswordChangeResponseBuilder();
    }

    public PasswordChangeResponseBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public PasswordChangeResponseBuilder withStatus(String status) {
        this.status = status;
        return this;
    }


    public PasswordChangeResponse build() {
        return new PasswordChangeResponse(email, status);
    }

}
