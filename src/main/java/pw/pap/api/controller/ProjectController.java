package pw.pap.api.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pw.pap.model.Project;
import pw.pap.model.Task;
import pw.pap.model.User;
import pw.pap.service.ProjectService;
import pw.pap.api.dto.ProjectAddDTO;
import pw.pap.api.dto.UserNameDTO;


@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = (List<Project>) projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProject(@PathVariable Long projectId) {
        Optional<Project> project = projectService.getProjectById(projectId);
        return project.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/add")
    public ResponseEntity<Project> addProject(@RequestBody ProjectAddDTO projectAddDTO) {
        Project addedProject = projectService.createProject(projectAddDTO.getName(), projectAddDTO.getOwner());
        return new ResponseEntity<>(addedProject, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<Task>> getProjectTasks(@PathVariable Long projectId) {
        List<Task> projectTasks = projectService.getProjectTasks(projectId);
        return new ResponseEntity<>(projectTasks, HttpStatus.OK);
    }

    @PutMapping("/update/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable Long projectId, @RequestBody Project updatedProject) {
        try {
            Project updated = projectService.updateProject(projectId, updatedProject);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        try {
            projectService.deleteProject(projectId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/assignUser/{projectId}")
    public ResponseEntity<User> assignUserToProject(@PathVariable Long projectId, @RequestBody UserNameDTO userNameDTO) {
        try {
            User user = projectService.assignUserToProject(projectId, userNameDTO.getName());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/removeUser/{projectId}")
    public ResponseEntity<Void> removeUserFromProject(@PathVariable Long projectId, @RequestBody UserNameDTO userNameDTO) {
        try {
            projectService.removeUserFromProject(projectId, userNameDTO.getName());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/removeTask/{projectId}/{taskId}")
    public ResponseEntity<Void> removeTaskFromProject(@PathVariable Long projectId, @PathVariable Long taskId) {
        try {
            projectService.removeTaskFromProject(projectId, taskId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
