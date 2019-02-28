package project.service;

import org.springframework.data.jpa.repository.JpaRepository;
import project.persistence.entities.Beer;
import project.persistence.entities.Comment;
import project.persistence.entities.User;

import java.util.List;

public interface CommentService {

    List<Comment> findByUser(User user);

    List<Comment> findByBeer(Beer beer);

    Comment save(Comment comment);
}
