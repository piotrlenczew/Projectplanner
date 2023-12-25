package pw.pap.api.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length=100)
    private String name;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "project_creation_date", nullable = false, updatable = false)
    private Date projectCreationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "project_deadline")
    private Date projectDeadline;

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    @ManyToMany
    @JoinTable(name = "project_user",
        joinColumns = {@JoinColumn(name = "project_id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> members = new HashSet<>();

    @OneToMany
    private Set<Task> tasks = new HashSet<>();

    public Project() {
        this.projectCreationDate = new Date();
    }

    public Project(String name, User owner, Date projectDeadline) {
        this.name = name;
        this.members.add(owner);
        this.owner = owner;
        this.projectDeadline = projectDeadline;
        this.projectCreationDate = new Date();
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

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Date getProjectCreationDate() {
        return projectCreationDate;
    }

    public void setProjectCreationDate(Date projectCreationDate) {
        this.projectCreationDate = projectCreationDate;
    }

    public Date getProjectDeadline() {
        return projectDeadline;
    }

    public void setProjectDeadline(Date projectDeadline) {
        this.projectDeadline = projectDeadline;
    }
}
