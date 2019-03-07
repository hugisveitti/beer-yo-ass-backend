package project.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.persistence.entities.Beer;
import project.persistence.entities.Comment;
import project.persistence.entities.User;
import project.persistence.repositories.CommentRepository;
import project.service.CommentService;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
        Beer beer = comment.getBeer();

        float beerStars = beer.getStars();
        int beerVotes = beer.getVotes();
        if(comment.getStars() >= 0 && comment.getStars() <= 5){
            if(beerVotes == 0){
                beerStars = comment.getStars();
            } else {
                // held ad thetta se rett formula til ad reikna beer stars.
                beerStars = (beerStars * beerVotes + comment.getStars()) / (beerVotes + 1);
            }
            beer.setStars(beerStars);
        }

        comment.setDate(new Date());

        beer.addComment(comment);
        User user = comment.getUser();
        user.addComment(comment);

        return commentRepository.save(comment);
    }


}
