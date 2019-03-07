package project.persistence.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Beeeeeeeeeeeeeeers are what the app is about.
 */

@Entity(name="Beer")
@Table(name="beers")
public class Beer {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "beer_id")
    private Long beerId;

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

    @Column(name = "beer_stars")
    private float stars=-1;

//    @Column(name = "beer_votes")
//    private int votes=0;

    @Column(name = "beer_price")
    private int price;

    @OneToMany(
            mappedBy = "beer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();


    public Beer(){    }




    public Long getBeerId() {
        return beerId;
    }

    public void setBeerId(Long beerId) {
        this.beerId = beerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkToVinbudin() {
        return linkToVinbudin;
    }

    public void setLinkToVinbudin(String linkToVinbudin) {
        this.linkToVinbudin = linkToVinbudin;
    }

    public float getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(float alcohol) {
        this.alcohol = alcohol;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public int getVotes() {
        return comments.size();
    }

//    public void setVotes(int votes) {
//        this.votes = votes;
//    }

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

    public List<ObjectNode> getPrettyComments(){
        List<ObjectNode> allComments = new ArrayList<>();
        for(int i=0; i<comments.size();i++){
            allComments.add(comments.get(i).getCommentJson());
        }
        return allComments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ObjectNode getJSONBeer(){
        final ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonBeer = mapper.createObjectNode();
        try{
            jsonBeer.put("name", name);
            jsonBeer.put("stars", stars);
            jsonBeer.put("alcohol", alcohol);
            jsonBeer.put("volume", volume);
            System.out.println(getPrettyComments());
            jsonBeer.putArray("comments").addAll(getPrettyComments());


            return jsonBeer;
        }catch (Exception e){
            System.out.println(e);
            return jsonBeer;
        }

    }







}
