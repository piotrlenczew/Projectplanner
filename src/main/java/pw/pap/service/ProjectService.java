package pw.pap.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.ArrayList;
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

    public Project createProject(String name, User owner) {
        LocalDateTime currentDate = LocalDateTime.now();
        Project project = new Project(name, owner, currentDate);
        return projectRepository.save(project);
    }

    public List<Task> getProjectTasks(Long projectId){
        List<Task> projectTasks = new ArrayList<>();
        Iterable<Task> tasks = taskRepository.findAll();

        for (Task task : tasks){
            if(task.getProject().getId().equals(projectId)){
                projectTasks.add(task);
            }
        }
        return projectTasks;
    }

    public Optional<Project> getProjectById(Long projectId) {
        return projectRepository.findById(projectId);
    }

    public Iterable<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project updateProject(Long projectId, Project updatedProject) {
        LocalDateTime currentDate = LocalDateTime.now();
        Project existingProject = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        String newName = updatedProject.getName();
        if (newName != null && !newName.equals(existingProject.getName())){
            if (updatedProject.getName().isBlank()) {
                throw new IllegalArgumentException("Project name cannot be empty");
            }
            existingProject.setName(newName);
        }

        String newDescription = updatedProject.getDescription();
        if (newDescription != null) {
            existingProject.setDescription(newDescription);
        }

        LocalDateTime newDeadline = updatedProject.getProjectDeadline();
        if (newDeadline != null) {
            if (!newDeadline.isAfter(currentDate)) {
                throw new IllegalArgumentException("New deadline must be after current time");
            }
            existingProject.setProjectDeadline(newDeadline);
        }

        if (updatedProject.getOwner() != null) {
            User newOwner = userRepository.findById(updatedProject.getOwner().getId())
                .orElseThrow(() -> new EntityNotFoundException("New owner not in database"));
            existingProject.setOwner(newOwner);
        }

        return projectRepository.save(existingProject);
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        Iterable<Task> tasks = taskRepository.findAll();
        for (Task task : tasks) {
            if(task.getProject().getId().equals(projectId)){
                taskRepository.deleteById(task.getId());
            }
        }

        projectRepository.deleteById(projectId);
    }

    @Transactional
    public void assignUserToProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        project.getMembers().add(user);
        projectRepository.save(project);
    }

    @Transactional
    public void removeUserFromProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Iterable<Task> tasks = taskRepository.findAll();
         for (Task task : tasks) {
             if (task.getProject().getId().equals(projectId)) {
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

        taskRepository.deleteById(taskId);
        projectRepository.save(project);
    }
}
