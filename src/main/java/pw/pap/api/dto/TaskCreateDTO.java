package pw.pap.api.dto;

public class TaskCreateDTO {
    private String title;
    private Long creatorId;
    private Long projectId;

    public String getTitle() {
        return title;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public Long getProjectId() {
        return projectId;
    }
}
