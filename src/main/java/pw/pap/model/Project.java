package pw.pap.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "Projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "project_creation_date", nullable = false, updatable = false)
    private LocalDateTime projectCreationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "project_deadline")
    private LocalDateTime projectDeadline;

    @ManyToMany
    @JoinTable(name = "project_user",
        joinColumns = {@JoinColumn(name = "project_id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    @JsonIgnore
    private List<User> members = new ArrayList<>();

    public Project() { }

    public Project(String name, User owner, LocalDateTime projectCreationDate) {
        this.name = name;
        this.owner = owner;
        this.members.add(owner);
        this.projectCreationDate = projectCreationDate;
    }

    public Project(String name, String description, LocalDateTime projectCreationDate, LocalDateTime projectDeadline, User owner, List<User> members) {
        this.name = name;
        this.description = description;
        this.projectCreationDate = projectCreationDate;
        this.projectDeadline = projectDeadline;
        this.owner = owner;
        this.members = members;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public LocalDateTime getProjectCreationDate() {
        return projectCreationDate;
    }

    public void setProjectCreationDate(LocalDateTime projectCreationDate) {
        this.projectCreationDate = projectCreationDate;
    }

    public LocalDateTime getProjectDeadline() {
        return projectDeadline;
    }

    public void setProjectDeadline(LocalDateTime projectDeadline) {
        this.projectDeadline = projectDeadline;
    }
}
