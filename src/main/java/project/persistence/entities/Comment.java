package project.persistence.entities;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long commentId;

    @OneToOne
    @JoinColumn(name="user_id")
    private long user_id;

    @OneToOne
    @JoinColumn(name="beer_id")
    private long beer_id;

    @Column(name="title")
    private String title;

    @Column(name="comment")
    private String comment;

    @Column(name="stars")
    private float stars;

    @Column(name="date")
    private String date;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getBeer_id() {
        return beer_id;
    }

    public void setBeer_id(long beer_id) {
        this.beer_id = beer_id;
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
}
