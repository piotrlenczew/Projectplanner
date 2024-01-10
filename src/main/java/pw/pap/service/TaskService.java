package pw.pap.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pw.pap.model.Task;
import pw.pap.model.User;
import pw.pap.model.Project;
import pw.pap.repository.TaskRepository;
import pw.pap.repository.UserRepository;
import pw.pap.repository.ProjectRepository;


@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public Task createTask(String title, Long creatorId, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!project.getMembers().contains(creator)) {
            throw new IllegalArgumentException("Member not found");
        }

        LocalDateTime currentDate = LocalDateTime.now();
        Task task = new Task(title, currentDate, creator, project);
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public Iterable<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        String newTitle = updatedTask.getTitle();
        if(newTitle.isBlank()){
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        existingTask.setTitle(newTitle);

        String newDescription = updatedTask.getDescription();
        existingTask.setDescription(newDescription);

        LocalDateTime newDeadline = updatedTask.getTaskDeadline();
        LocalDateTime currentDate = LocalDateTime.now();
        if (!newDeadline.isAfter(currentDate)) {
            throw new IllegalArgumentException("New deadline must be after current time");
        }
        existingTask.setTaskDeadline(newDeadline);

        taskRepository.deleteById(taskId);
        updatedTask.setId(taskId);
        return taskRepository.save(updatedTask);
    }

    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        Project project = task.getProject();

        taskRepository.deleteById(taskId);
    }

    @Transactional
    public void assignUserToTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!task.getProject().getMembers().contains(user)) {
            throw new IllegalArgumentException("Member not found");
        }
        else {
            task.getAssignees().add(user);
            taskRepository.save(task);
            userRepository.save(user);
        }
    }

    @Transactional
    public void removeUserFromTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        task.getAssignees().remove(user);
        taskRepository.save(task);
    }
}
