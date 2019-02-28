package project.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.persistence.entities.Beer;
import project.persistence.entities.Comment;
import project.persistence.entities.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBeer(Beer beer);

    List<Comment> findByUser(User user);

    Comment save(Comment comment);

    void delete(Comment comment);
}
