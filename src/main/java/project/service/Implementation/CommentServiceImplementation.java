package project.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.persistence.entities.Beer;
import project.persistence.entities.Comment;
import project.persistence.entities.User;
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

    public List<Comment> findByBeer(Beer beer){
        return commentRepository.findByBeer(beer);
    }

    public List<Comment> findByUser(User user){
        return commentRepository.findByUser(user);
    }


    public Comment save(Comment comment){
        return commentRepository.save(comment);
    }


}
