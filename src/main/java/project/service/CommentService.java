package project.service;

import org.springframework.data.jpa.repository.JpaRepository;
import project.persistence.entities.Beer;
import project.persistence.entities.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findByUserId(Long userId);

    List<Comment> findByBeerId(Long beerId);
}
