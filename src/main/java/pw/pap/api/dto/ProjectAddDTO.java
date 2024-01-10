package pw.pap.api.dto;

import pw.pap.model.User;


public class ProjectAddDTO {
    private String name;
    private User owner;

    public ProjectAddDTO() { }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }
}
