package project.persistence.entities;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


import javax.persistence.*;
import java.util.Date;

/**
 * When a user comments on a beer or leaves a rating (stars).
 * Should be possible to comment without leaving a rating and
 * leaving a rating without commenting.
 */


@Entity
@Table(name = "comments")
public class Comment {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="beer_id")
    private Beer beer;

    @Column(name="title")
    private String title;

    @Column(name="comment")
    private String comment;

    //stars are floats out of 5.
    @Column(name="stars")
    private float stars;

    @Column(name="date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;



    public Comment(){}

    public Comment(User user, Beer beer, String title, String comment, float stars){
        this.user = user;
        this.beer = beer;
        this.title = title;
        this.comment = comment;
        this.stars = stars;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    //get a JSON of the comment without a user object.
    public ObjectNode getCommentJson(){
        final ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        try{
            json.put("title", title);
            json.put("comment", comment);
            json.put("username", user.getUsername());
            json.put("userId", user.getId());
            json.put("beerId", beer.getBeerId());
            json.put("beername", beer.getName());
            json.put("date", date.toString());
            json.put("stars", stars);
            json.put("commentId", commentId);
            return json;
        } catch(Exception e){
            System.out.println(e);
            return json;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment )) return false;
        return commentId != null && commentId.equals(((Comment) o).commentId);
    }
    @Override
    public int hashCode() {
        return 31;
    }
}
