package project.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.persistence.entities.Beer;
import project.persistence.entities.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Beer, Long> {

    List<Comment> findByBeerId(Long beerId);

    List<Comment> findByUserId(Long userId);

}
