package project.persistence.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Beeeeeeeeeeeeeeers are what the app is about.
 *
 * getPrettyComments is used to get information about the comment
 * without getting the user but just the username, if we don't do this
 * we get an endless reference loop (because the user has comments and comments have users).
 *
 * getJSONBeer returns a JSON object with or without getPrettyComments Array..
 *
 */

@Entity(name="Beer")
@Table(name="beers")
public class Beer {


    @Id
    @Column(name = "beer_id")
    private String beerId;

    @Column(name = "beer_name")
    private String name;

    @Column(name = "beer_link")
    private String linkToVinbudin;

    @Column(name = "beer_alcohol")
    private float alcohol;

    @Column(name = "beer_taste")
    private String taste;

    @Column(name = "beer_volume")
    private int volume;

    //if -1 then it has never been voted on.
    @Column(name = "beer_stars")
    private float stars=-1;

    @Column(name = "beer_price")
    private int price;


    //a beer can have many comments but one comment can have one beer
    @OneToMany(
            mappedBy = "beer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();


    public Beer(){    }




    public String getBeerId() {
        return beerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    //only add to votes if comment stars is not -1
    public int getVotes() {
        int count = 0;
        for(int i=0; i<comments.size(); i++){
            if(comments.get(i).getStars() != -1){
                count += 1;
            }
        }
        return count;
    }


    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment){
        comments.add(comment);
        comment.setBeer(this);
    }

    public void removeComment(Comment comment){
        comments.remove(comment);
        comment.setBeer(null);
    }

    /*
    verðum að nota ObjectNode og sækja sérstakt json sem er getComments,
    því ef við notum comment beint þá er það með reference í user, sem er með reference í comment
    o.s.frv. endalaust.
     */
    @Transient
    public List<ObjectNode> getPrettyComments(){
        List<ObjectNode> allComments = new ArrayList<>();
        for(int i=0; i<comments.size();i++){
            allComments.add(comments.get(i).getCommentJson());
        }
        return allComments;
    }


    /*
    Verð að nota þetta ObjectNode þar sem það virkar ekki að senda JSONbeer
     */
    @Transient
    public ObjectNode getJSONBeer(boolean withComments){
        final ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonBeer = mapper.createObjectNode();
        try{
            jsonBeer.put("name", name);
            jsonBeer.put("stars", stars);
            jsonBeer.put("alcohol", alcohol);
            jsonBeer.put("volume", volume);
            jsonBeer.put("linkToVinbudin", linkToVinbudin);
            jsonBeer.put("taste", taste);
            jsonBeer.put("price", price);
            jsonBeer.put("beerId",beerId);
            System.out.println(getPrettyComments());
            if(withComments) {
                jsonBeer.putArray("comments").addAll(getPrettyComments());
            }
            return jsonBeer;
        }catch (Exception e){
            System.out.println(e);
            return jsonBeer;
        }
    }

}
