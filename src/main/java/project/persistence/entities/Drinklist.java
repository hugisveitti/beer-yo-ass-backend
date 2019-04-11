package project.persistence.entities;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name="Drinklist")
@Table(name="drinklist")
public class Drinklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="drinklist_id")
    private Long drinklistId;

    @Column(name="name")
    private String name;


    //drinklist has one user, but one user can have many drinklists
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;


    @ManyToMany
    @JoinTable(
            name = "unchecked_beers",
            joinColumns = @JoinColumn(
                    name = "drinklist_id", referencedColumnName = "drinklist_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "beer_id", referencedColumnName = "beer_id"))
    private List<Beer> uncheckedBeers = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "checked_beers",
            joinColumns = @JoinColumn(
                    name = "drinklist_id", referencedColumnName = "drinklist_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "beer_id", referencedColumnName = "beer_id"))
    private List<Beer> checkedBeers = new ArrayList<>();


    //is the drinklist avaliable for others to see and download
    @Column(name="is_public")
    private boolean isPublic;


    public Drinklist(){}

    public Drinklist(String name, boolean isPublic){
        this.name = name;
        this.isPublic = isPublic;
    }

    public Drinklist(String name, List<Beer> checkedBeers, List<Beer> uncheckedBeers, boolean isPublic){
        this.name = name;
        this.checkedBeers = checkedBeers;
        this.uncheckedBeers = uncheckedBeers;
        this.isPublic = isPublic;
    }


    public void addBeer(Beer beer){
        uncheckedBeers.add(beer);
    }

    public void checkBeer(Beer beer){
        for(int i=0; i<uncheckedBeers.size();i++){
            if(uncheckedBeers.get(i) == beer){
                checkedBeers.add(beer);
                uncheckedBeers.remove(i);
                break;
            }
        }
    }

    public void uncheckBeer(Beer beer){
        for(int i=0; i<checkedBeers.size();i++){
            if(checkedBeers.get(i) == beer){
                uncheckedBeers.add(beer);
                checkedBeers.remove(i);
                break;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setUser(User user){
        this.user = user;
    }


    public User getUser() {
        return user;
    }

    public List<Beer> getUncheckedBeers() {
        return uncheckedBeers;
    }

    public void setUncheckedBeers(List<Beer> uncheckedBeers) {
        this.uncheckedBeers = uncheckedBeers;
    }

    public List<Beer> getCheckedBeers() {
        return checkedBeers;
    }

    public void setCheckedBeers(List<Beer> checkedBeers) {
        this.checkedBeers = checkedBeers;
    }

    public Long getDrinklistId() {
        return drinklistId;
    }

    public void setDrinklistId(Long drinklistId) {
        this.drinklistId = drinklistId;
    }

    //when the user opens a drinklist, he will see two types of beer lists, one with checked beers and one with unchecked beers.
    public List<ObjectNode> getObjectNodeuncheckedBeers(){
        List<ObjectNode> allBeers = new ArrayList<>();
        for(int i=0; i<uncheckedBeers.size(); i++){

            //maybe show him comments on his beer, or the only place to see the comments will be on each beers individual page.
            allBeers.add(uncheckedBeers.get(i).getJSONBeer(false));
        }
        return allBeers;
    }

    //when the user goes to "my page" and wants to see the beers he has saved.
    public List<ObjectNode> getObjectNodecheckedBeers(){
        List<ObjectNode> allBeers = new ArrayList<>();
        for(int i=0; i<checkedBeers.size(); i++){

            //maybe show him comments on his beer, or the only place to see the comments will be on each beers individual page.
            allBeers.add(checkedBeers.get(i).getJSONBeer(false));
        }
        return allBeers;
    }


    /*
    Verð að nota þetta ObjectNode þar sem það virkar ekki að senda JSONbeer
     */
    @Transient
    public ObjectNode getJSONDrinklist(){
        final ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonDrinklist = mapper.createObjectNode();
        try{
            jsonDrinklist.put("name", name);
            jsonDrinklist.put("user", user.getUsername());
            jsonDrinklist.put("isPublic", isPublic);
            jsonDrinklist.put("drinklistId", drinklistId);
            List<ObjectNode> allUncheckedBeers = new ArrayList<>();
            for(int i=0; i<uncheckedBeers.size(); i++){
                allUncheckedBeers.add(uncheckedBeers.get(i).getJSONBeer(false));
            }
            jsonDrinklist.putArray("uncheckedBeers").addAll(allUncheckedBeers);

            List<ObjectNode> allCheckedBeers = new ArrayList<>();
            for(int i=0; i<checkedBeers.size(); i++){
                allCheckedBeers.add(checkedBeers.get(i).getJSONBeer(false));
            }
            jsonDrinklist.putArray("checkedBeers").addAll(allCheckedBeers);



            return jsonDrinklist;
        }catch (Exception e){
            System.out.println(e);
            return jsonDrinklist;
        }
    }
}
