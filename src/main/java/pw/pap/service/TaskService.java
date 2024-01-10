package pw.pap.service;

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

    public Task createTask(String title, String description, User creator, Project project, List<User> assignees, LocalDateTime taskDeadline) {
        Project foundProject = projectRepository.findById(project.getId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        User user = userRepository.findById(creator.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!project.getMembers().contains(creator)) {
            throw new IllegalArgumentException("Member not found");
        }

        for (User assignee : assignees){
            user = userRepository.findById(assignee.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            if (!project.getMembers().contains(user)) {
                throw new IllegalArgumentException("Member not found");
            }
        }
        LocalDateTime currentDate = LocalDateTime.now();
        Task task = new Task(title, description, currentDate, taskDeadline, assignees, creator, foundProject);
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

        taskRepository.deleteById(taskId);
        updatedTask.setId(taskId);
        return taskRepository.save(updatedTask);
    }

    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        Project project = task.getProject();
        project.getTasks().remove(task);

        User creator = task.getCreator();
        creator.getCreatedTasks().remove(task);

        List<User> assignees = task.getAssignees();
        for (User assignee : assignees) {
            assignee.getAssignedTasks().remove(task);
        }

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
            user.getAssignedTasks().add(task);
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
        user.getAssignedTasks().remove(task);
        taskRepository.save(task);
    }
}
