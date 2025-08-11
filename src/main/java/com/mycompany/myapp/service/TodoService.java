package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Todo;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.TodoRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.dto.TodoDTO;
import com.mycompany.myapp.service.mapper.TodoMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Todo}.
 */
@Service
public class TodoService {

    private final Logger log = LoggerFactory.getLogger(TodoService.class);

    private final TodoRepository todoRepository;

    private final TodoMapper todoMapper;

    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, TodoMapper todoMapper, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
        this.userRepository = userRepository;
    }

    /**
     * Save a todo.
     *
     * @param todoDTO the entity to save.
     * @return the persisted entity.
     */
    public TodoDTO save(TodoDTO todoDTO) {
        log.debug("Request to save Todo : {}", todoDTO);
        
        Todo todo = todoMapper.toEntity(todoDTO);
        
        // Set the current user if not already set
        if (todo.getUser() == null) {
            String currentUserLogin = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("Current user login not found"));
            User user = userRepository.findOneByLogin(currentUserLogin)
                .orElseThrow(() -> new RuntimeException("User not found"));
            todo.setUser(user);
        }
        
        todo = todoRepository.save(todo);
        return todoMapper.toDto(todo);
    }

    /**
     * Update a todo.
     *
     * @param todoDTO the entity to save.
     * @return the persisted entity.
     */
    public TodoDTO update(TodoDTO todoDTO) {
        log.debug("Request to update Todo : {}", todoDTO);
        
        Todo todo = todoMapper.toEntity(todoDTO);
        todo = todoRepository.save(todo);
        return todoMapper.toDto(todo);
    }

    /**
     * Partially update a todo.
     *
     * @param todoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TodoDTO> partialUpdate(TodoDTO todoDTO) {
        log.debug("Request to partially update Todo : {}", todoDTO);

        return todoRepository
            .findById(todoDTO.getId())
            .map(existingTodo -> {
                todoMapper.partialUpdate(existingTodo, todoDTO);
                return existingTodo;
            })
            .map(todoRepository::save)
            .map(todoMapper::toDto);
    }

    /**
     * Get all the todos.
     *
     * @return the list of entities.
     */
    public List<TodoDTO> findAll() {
        log.debug("Request to get all Todos");
        return todoRepository.findAll().stream().map(todoMapper::toDto).toList();
    }

    /**
     * Get one todo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<TodoDTO> findOne(String id) {
        log.debug("Request to get Todo : {}", id);
        return todoRepository.findById(id).map(todoMapper::toDto);
    }

    /**
     * Delete the todo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Todo : {}", id);
        todoRepository.deleteById(id);
    }

    /**
     * Mark a todo as completed.
     *
     * @param id the id of the todo to mark as completed.
     * @return the updated todo.
     */
    public Optional<TodoDTO> markAsCompleted(String id) {
        log.debug("Request to mark Todo as completed : {}", id);
        
        return todoRepository
            .findById(id)
            .map(todo -> {
                todo.setCompleted(true);
                return todoRepository.save(todo);
            })
            .map(todoMapper::toDto);
    }

    /**
     * Toggle the completion status of a todo.
     *
     * @param id the id of the todo to toggle.
     * @return the updated todo.
     */
    public Optional<TodoDTO> toggleCompleted(String id) {
        log.debug("Request to toggle Todo completion status : {}", id);
        
        return todoRepository
            .findById(id)
            .map(todo -> {
                todo.setCompleted(!Boolean.TRUE.equals(todo.getCompleted()));
                return todoRepository.save(todo);
            })
            .map(todoMapper::toDto);
    }

    /**
     * Get all todos for the current user.
     *
     * @return the list of todos for the current user.
     */
    public List<TodoDTO> findByCurrentUser() {
        log.debug("Request to get all Todos for current user");
        
        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Current user login not found"));
        
        return todoRepository
            .findByUserLoginOrderByCreatedDateDesc(currentUserLogin)
            .stream()
            .map(todoMapper::toDto)
            .toList();
    }

    /**
     * Get todos for the current user filtered by completion status.
     *
     * @param completed the completion status to filter by.
     * @return the list of todos for the current user with the specified completion status.
     */
    public List<TodoDTO> findByCurrentUserAndCompleted(Boolean completed) {
        log.debug("Request to get Todos for current user with completed status : {}", completed);
        
        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Current user login not found"));
        
        return todoRepository
            .findByUserLoginAndCompleted(currentUserLogin, completed)
            .stream()
            .map(todoMapper::toDto)
            .toList();
    }
}
