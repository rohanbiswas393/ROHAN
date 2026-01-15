package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Todo;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the {@link Todo} entity.
 */
@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {
    /**
     * Find all todos by user login ordered by created date descending.
     *
     * @param login the user login
     * @return the list of todos
     */
    List<Todo> findByUserLoginOrderByCreatedDateDesc(String login);

    /**
     * Find todos by user login and completion status.
     *
     * @param login the user login
     * @param completed the completion status
     * @return the list of todos
     */
    List<Todo> findByUserLoginAndCompleted(String login, Boolean completed);
}
