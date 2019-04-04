package project.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.persistence.entities.Beer;
import project.persistence.entities.Comment;
import project.persistence.entities.User;
import project.persistence.repositories.BeerRepository;
import project.persistence.repositories.CommentRepository;
import project.persistence.repositories.UserRepository;
import project.service.CommentService;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImplementation implements CommentService {

    CommentRepository commentRepository;
    BeerRepository beerRepository;
    UserRepository userRepository;

    @Autowired
    public CommentServiceImplementation(CommentRepository commentRepository, BeerRepository beerRepository, UserRepository userRepository){
        this.commentRepository = commentRepository;
        this.beerRepository = beerRepository;
        this.userRepository = userRepository;
    }

    public List<Comment> findByBeer(Beer beer){
        return commentRepository.findByBeer(beer);
    }

    public List<Comment> findByUser(User user){
        return commentRepository.findByUser(user);
    }


    /*
    Saving a comment.
    Update the stars on the beer.
    add the comment to the beer.
    add the comment to the user.
    Save the comment to the repo.
     */
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


    //delete star from beer
    public void delete(Long commentId){
        System.out.println(commentId);
        Comment currComment = commentRepository.findByCommentId(commentId);
        Beer beer = currComment.getBeer();
        float allRating = beer.getStars()*beer.getVotes();
        float commStars = currComment.getStars();

        //if no stars on comment just remove comment..
        if(commStars != -1) {
            allRating = allRating - commStars;

            beer.removeComment(currComment);
            System.out.println("beer votes " + beer.getVotes());
            if (beer.getVotes() != 0) {
                System.out.println("has votes");
                beer.setStars(allRating / beer.getVotes());
            } else {
                beer.setStars(-1);
            }
        } else {
            beer.removeComment(currComment);
        }
        beerRepository.save(beer);


        commentRepository.delete(currComment);
    }

    public void update(Long commentId, String title, String comment, float stars){
        Comment currComment = commentRepository.findByCommentId(commentId);

        //update value of beer stars.
        float oldStars = currComment.getStars();
        if(oldStars == -1){
            oldStars = 0;
        }

        Beer beer = currComment.getBeer();

        //think this is correct..
        float allRating =  beer.getStars()*beer.getVotes();
        allRating = allRating - oldStars + stars;

        beer.setStars(allRating/beer.getVotes());

        beerRepository.save(beer);

        currComment.setTitle(title);
        currComment.setComment(comment);
        currComment.setStars(stars);
        commentRepository.save(currComment);
    }
}
