package pw.pap.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.pap.model.Project;
import pw.pap.model.User;
import pw.pap.model.Task;
import pw.pap.repository.TaskRepository;
import pw.pap.repository.UserRepository;
import pw.pap.repository.ProjectRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public Project createProject(String name, String description, LocalDateTime projectDeadline, User owner, List<User> members) {
        LocalDateTime currentDate = LocalDateTime.now();
        Project project = new Project(name, description, currentDate, projectDeadline, owner, members);
        return projectRepository.save(project);
    }

    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

    public Optional<Project> getProjectById(Long projectId) {
        return projectRepository.findById(projectId);
    }

    public Iterable<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project updateProject(Long projectId, Project updatedProject) {
        Project existingProject = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        projectRepository.deleteById(projectId);
        updatedProject.setId(projectId);
        return projectRepository.save(updatedProject);
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        User owner = project.getOwner();
        owner.getOwnedProjects().remove(project);

        List<User> members = project.getMembers();
        for (User member : members) {
            member.getMemberOfProjects().remove(project);
        }

        List<Task> tasks = project.getTasks();
        for (Task task : tasks) {
            taskRepository.deleteById(task.getId());
        }

        projectRepository.deleteById(projectId);
    }

    @Transactional
    public void assignUserToProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.getMemberOfProjects().add(project);
        userRepository.save(user);
        project.getMembers().add(user);
        projectRepository.save(project);
    }

    @Transactional
    public void removeUserFromProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Task> assignedTasks = user.getAssignedTasks();
        for (Task task : assignedTasks) {
            if (task.getProject().equals(project)) {
                task.getAssignees().remove(user);
            }
        }

        project.getMembers().remove(user);
        projectRepository.save(project);
    }

    @Transactional
    public void removeTaskFromProject(Long projectId, Long taskId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        User creator = task.getCreator();
        creator.getCreatedTasks().remove(task);

        List<User> assignees = task.getAssignees();
        for (User assignee : assignees) {
            assignee.getAssignedTasks().remove(task);
        }

        project.getTasks().remove(task);
        taskRepository.deleteById(taskId);
        projectRepository.save(project);
    }
}
