package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Todo;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.TodoDTO;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link Todo} and its DTO {@link TodoDTO}.
 */
@Service
public class TodoMapper {

    public List<TodoDTO> toDto(List<Todo> entityList) {
        return entityList.stream().filter(Objects::nonNull).map(this::toDto).collect(Collectors.toList());
    }

    public List<Todo> toEntity(List<TodoDTO> dtoList) {
        return dtoList.stream().filter(Objects::nonNull).map(this::toEntity).collect(Collectors.toList());
    }

    public TodoDTO toDto(Todo entity) {
        if (entity == null) {
            return null;
        }

        TodoDTO dto = new TodoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setCompleted(entity.getCompleted());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUserLogin(entity.getUser().getLogin());
        }

        return dto;
    }

    public Todo toEntity(TodoDTO dto) {
        if (dto == null) {
            return null;
        }

        Todo entity = new Todo();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setCompleted(dto.getCompleted());

        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            user.setLogin(dto.getUserLogin());
            entity.setUser(user);
        }

        return entity;
    }

    public Todo partialUpdate(Todo entity, TodoDTO dto) {
        if (dto == null) {
            return entity;
        }

        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getCompleted() != null) {
            entity.setCompleted(dto.getCompleted());
        }

        return entity;
    }
}
