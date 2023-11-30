package account.model.user;

import org.junit.Test;

import static org.junit.Assert.*;

public class RoleTest {

    @Test
    public void getChangePassRoles() {
        String[] expectedRoles = {"USER", "ACCOUNTANT", "ADMINISTRATOR"};
        assertArrayEquals(expectedRoles, Role.getChangePassRoles());
    }

    @Test
    public void getPaymentRoles() {
        String[] expectedRoles = {"USER", "ACCOUNTANT"};
        assertArrayEquals(expectedRoles, Role.getPaymentRoles());
    }
}