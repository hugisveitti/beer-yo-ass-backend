package project.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.persistence.entities.Comment;
import project.persistence.repositories.CommentRepository;
import project.service.CommentService;

import java.util.List;

@Service
public class CommentServiceImplementation implements CommentService {

    CommentRepository commentRepository;

    @Autowired
    public CommentServiceImplementation(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    List<Comment> findByBeerId(Long beerId){
        return commentRepository.findByBeerId(beerId);
    }

    List<Comment> findByUserId(Long userId){
        return commentRepository.findByUserId(userId);
    }

}
