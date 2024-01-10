package pw.pap.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pw.pap.model.Project;
import pw.pap.model.User;
import pw.pap.model.Task;
import pw.pap.repository.ProjectRepository;
import pw.pap.repository.UserRepository;
import pw.pap.repository.TaskRepository;


@Service
public class UserService {
    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;

    @Autowired
    public UserService(ProjectRepository projectRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public User login(String name, String enteredPassword) {
        User user = findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("User not in database"));

        if (authenticateUser(user, enteredPassword)) {
            return user;
        }
        throw new IllegalArgumentException("Wrong password");
    }

    public User register(String name, String password) {
        Optional<User> optionalUser = findByName(name);

        if (optionalUser.isPresent()) {
            throw new EntityExistsException("User with the same name already in the database");
        }
        String salt = generateRandomSalt();
        String hashedPassword = hashPasswordWithSalt(password, salt);
        LocalDateTime currentDate = LocalDateTime.now();
        User user = new User(name, hashedPassword, salt, currentDate);
        userRepository.save(user);
        return user;
    }

    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userRepository.deleteById(userId);
        updatedUser.setId(userId);
        return userRepository.save(updatedUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Project> memberOfProjects = user.getMemberOfProjects();
        for (Project project : memberOfProjects) {
            project.getMembers().remove(user);
        }

        List<Project> ownerOfProjects = user.getOwnedProjects();
        for (Project project : ownerOfProjects) {
            project.getMembers().remove(user);
            if (project.getMembers().isEmpty()) {
                projectRepository.deleteById(project.getId());
            }
            else{
                project.setOwner(project.getMembers().get(0));
            }
        }

        List<Task> assignedTasks = user.getAssignedTasks();
        for (Task task : assignedTasks) {
            task.getAssignees().remove(user);
        }

        List<Task> createdTasks = user.getCreatedTasks();
        for (Task task : createdTasks) {
            task.setCreator(null);
        }

        userRepository.deleteById(userId);
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }


    public Optional<User> findByName(String name) {
        for(User user : userRepository.findAll())
            if(user.getName().equals(name)){
                return Optional.of(user);
            }
        return Optional.empty();
    }

    private String generateRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);

        return Base64.getEncoder().encodeToString(saltBytes);
    }

    private String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    private String hashPasswordWithSalt(String password, String salt) {
        String saltedPassword = password + salt;
        return hashPassword(saltedPassword);
    }

    public boolean authenticateUser(User user, String enteredPassword) {
        String saltedPassword = enteredPassword + user.getSalt();
        return new BCryptPasswordEncoder().matches(saltedPassword, user.getPasswordHash());
    }
}
