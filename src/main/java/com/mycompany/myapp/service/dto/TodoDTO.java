package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Todo} entity.
 */
public class TodoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @NotNull
    @Size(min = 1, max = 200)
    private String title;

    @Size(max = 1000)
    private String description;

    private Boolean completed;

    private String userLogin;

    private String userId;

    private Instant createdDate;

    private Instant lastModifiedDate;

    public TodoDTO() {
        // Empty constructor needed for Jackson.
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TodoDTO)) {
            return false;
        }

        TodoDTO todoDTO = (TodoDTO) o;
        if (this.id == null) {
            return false;
        }
        return this.id.equals(todoDTO.id);
    }

    @Override
    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TodoDTO{" +
            "id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", completed='" + getCompleted() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            ", userId='" + getUserId() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
