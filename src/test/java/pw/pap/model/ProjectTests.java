package pw.pap.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import pw.pap.repository.ProjectRepository;
import pw.pap.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProjectTests {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final LocalDateTime date = LocalDateTime.parse("2023-10-11 13:37:42", formatter);
    private final LocalDateTime deadlineDate = LocalDateTime.parse("2023-10-21 13:37:42", formatter);

    @Test
    public void testEmptyConstructor() {
        Project project = new Project();
        assertThrows(DataIntegrityViolationException.class, () -> projectRepository.save(project));
    }

    @Test
    public void testNameConstructor() {
        User user = new User("Rob", date);
        Project project = new Project("Shop", user,  date);
        assertEquals("Shop", project.getName());
        assertEquals(user, project.getOwner());
        assertEquals(date, project.getProjectCreationDate());
    }

    @Test
    public void testFullConstructor() {
        User user = new User("Rob", date);
        List<User> members = new ArrayList<>(Arrays.asList(user));
        Project project = new Project("Shop", "A clothes shop", date, deadlineDate, user, members);
        assertEquals("Shop", project.getName());
        assertEquals("A clothes shop", project.getDescription());
        assertEquals(date, project.getProjectCreationDate());
        assertEquals(deadlineDate, project.getProjectDeadline());
        assertEquals(user, project.getOwner());
        assertEquals(members, project.getMembers());

    }

    @Test
    @Transactional
    @Rollback
    public void testSaveProjectWithoutSavedUser() {
        User user = new User("Rob", date);
        List<User> members = new ArrayList<>(Arrays.asList(user));
        Project project = new Project("Shop", "A clothes shop", date, deadlineDate, user, members);
        assertThrows(InvalidDataAccessApiUsageException.class, () -> projectRepository.save(project));
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveRetrieveDeleteProject() {
        User user = new User("Rob", date);
        List<User> members = new ArrayList<>(Arrays.asList(user));
        Project project = new Project("Shop", "A clothes shop", date, deadlineDate, user, members);
        User savedUser = userRepository.save(user);
        Project savedProject = projectRepository.save(project);
        Project retrievedProject = projectRepository
                .findById(savedProject.getId())
                .orElse(null);

        assertNotNull(retrievedProject);
        assertEquals("Shop", retrievedProject.getName());
        assertEquals("A clothes shop", retrievedProject.getDescription());
        assertEquals(date, retrievedProject.getProjectCreationDate());
        assertEquals(deadlineDate, retrievedProject.getProjectDeadline());
        assertEquals(user, retrievedProject.getOwner());
        assertEquals(members, retrievedProject.getMembers());

        projectRepository.deleteById(retrievedProject.getId());
        retrievedProject = projectRepository
                .findById(savedProject.getId())
                .orElse(null);
        assertNull(retrievedProject);

        User retrievedUser = userRepository
                .findById(savedUser.getId())
                .orElse(null);
        assertNotNull(retrievedUser);
        userRepository.deleteById(retrievedUser.getId());
    }
}
