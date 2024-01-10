package pw.pap.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import pw.pap.model.Project;
import pw.pap.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProjectServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final LocalDateTime date = LocalDateTime.parse("2023-10-11 13:37:42", formatter);

     @Test
     @Rollback
     public void testCreateProject() {
         String username = "testBob";
         String password = "reallySecurePassword";
         User user = userService.register(username, password);

         try {
             Project project = projectService.createProject("roller coaster", user);
             Project projectInDatabase = projectService.getProjectById(project.getId()).orElse(null);
             assertNotNull(projectInDatabase, "Project not in database");

             assertEquals(project.getId(), projectInDatabase.getId());
         } catch (Exception e) {
             fail("Exception occurred during login: " + e.getMessage());
         } finally {
             userService.deleteUser(user.getId());
         }
     }
}
