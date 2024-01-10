package pw.pap.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import pw.pap.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    @Rollback
    public void testRegister() {
        String username = "testBob";
        String password = "reallySecurePassword";
        userService.register(username, password);

        User userInDatabase = userService.findByName(username).orElse(null);
        assertNotNull(userInDatabase, "User not in database");

        assertEquals(userInDatabase.getName(), "testBob", "Different name in database.");
        assertNotEquals(userInDatabase.getPasswordHash(), "reallySecurePassword", "Hashed password is the same as given password.");
        userService.deleteUser(userInDatabase.getId());
    }

    @Test
    @Rollback
    public void testRegisterExistingName() {
        String username = "testBob";
        String password = "reallySecurePassword";
        User firstUser = userService.register(username, password);
        assertThrows(EntityExistsException.class, () -> userService.register(username, password));
        userService.deleteUser(firstUser.getId());
    }

    @Test
    @Rollback
    public void testLogin() {
        String username = "testBob";
        String password = "reallySecurePassword";
        User registeredUser = userService.register(username, password);

        try {
            User loggedInUser = userService.login(username, password);
            assertEquals(registeredUser.getName(), loggedInUser.getName(), "Incorrect return name");
        } catch (Exception e) {
            fail("Exception occurred during login: " + e.getMessage());
        } finally {
            userService.deleteUser(registeredUser.getId());
        }
    }

    @Test
    @Rollback
    public void testLoginUserNotInDatabase() {
        assertThrows(EntityNotFoundException.class, () -> userService.login("hvdfi14iicvg6575xzhccd342scjba574sgu87xscgas7vhc", "Password"));
    }

    @Test
    @Rollback
    public void testLoginWrongPassword() {
        String username = "testBob";
        String password = "reallySecurePassword";
        User registeredUser = userService.register(username, password);
        assertThrows(IllegalArgumentException.class, () -> userService.login(username, "notSecurePassword"));
        userService.deleteUser(registeredUser.getId());
    }

    @Test
    @Rollback
    public void testUpdate() {
        String username = "testBob";
        String password = "reallySecurePassword";
        User user = userService.register(username, password);
        user.setName("testRonald");
        userService.updateUser(user.getId(), user);

        User updatedUser = userService.getUserById(user.getId());
        assertEquals(updatedUser.getName(), "testRonald");
    }
}
