package project.persistence.entities;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

/**
 * Maybe we should implement User form spring core instead?
 *
 * By implementing {@link UserDetails} we can use spring core for Authentication
 * The class for the User itself.
 * The system generates a table schema based on this class for this entity.
 * Be sure to annotate any entities you have with the @Entity annotation.
 */
@Entity
@Table(name = "users") // If you want to specify a table name, you can do so here
public class User implements UserDetails {




    // Declare that this attribute is the id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column(unique=true, name="username")
    private String username;

    private String password;


    @Column(name="enabled")
    private Boolean enabled;


    //profile picture is a beer id, so users identify with a beer
    @Column(name="profile_picture")
    private String profilePicture;



    @ManyToMany
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(
                name = "username", referencedColumnName = "username"),
        inverseJoinColumns = @JoinColumn(
                name = "role", referencedColumnName = "role"))
    private Set<Role> roles;


    @ManyToMany
    @JoinTable(
            name = "my_beers",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "beer_id", referencedColumnName = "beer_id"))
    private List<Beer> myBeers = new ArrayList<>();



    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();


    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Drinklist> drinklists = new ArrayList<>();



    public User(){}

    public User(String username, String password) {
        this.username = username;
        this.password = password;

        this.enabled = true;
    }

    //UserDetails makes you implement this method, used for authentication.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Set<Role> userRoles = this.getRoles();
        if(userRoles != null)
        {
            for (Role role : userRoles) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRole());
                authorities.add(authority);
            }
        }
        return authorities;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment){
        comments.add(comment);
        comment.setUser(this);
    }

    public void removeComment(Comment comment){
        comments.remove(comment);
        comment.setUser(null);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    public List<Beer> getMyBeers() {
        return myBeers;
    }

    //when the user goes to "my page" and wants to see the beers he has saved.
    public List<ObjectNode> getObjectNodeMyBeers(){
        List<ObjectNode> allBeers = new ArrayList<>();
        for(int i=0; i<myBeers.size(); i++){

            //maybe show him comments on his beer, or the only place to see the comments will be on each beers individual page.
            allBeers.add(myBeers.get(i).getJSONBeer(true));
        }
        return allBeers;
    }

    public void addToMyBeers(Beer beer){
        myBeers.add(beer);
    }

    public void setMyBeers(List<Beer> myBeers){
        this.myBeers = myBeers;
    }

    public void removeFromMyBeers(Beer beer){
        myBeers.remove(beer);
    }


    public void addDrinklist(Drinklist drinklist){
        drinklists.add(drinklist);
        drinklist.setUser(this);
    }

    public void removeDrinklist(Drinklist drinklist){
        drinklists.remove(drinklist);
    }

    //when the user goes to "my page" and wants to see the beers he has saved.
    public List<ObjectNode> getObjectNodeMyDrinklist(){
        List<ObjectNode> allDrinklists = new ArrayList<>();
        for(int i=0; i<drinklists.size(); i++){

            //maybe show him comments on his beer, or the only place to see the comments will be on each beers individual page.
            allDrinklists.add(drinklists.get(i).getJSONDrinklist());
        }
        return allDrinklists;
    }



    @Override
    public String toString(){
        return "User " + this.username + " has id " + this.id + " and password " + this.password;
    }



    //ATH BUA TIL VARIABLES FYRIR THESSI METHODS TIL AD SKILA
    //TODO LAGA THESSA BOLEAN DAEMI
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return enabled;
    }

    public void setEnabled(Boolean enabled){
        this.enabled = enabled;
    }

}