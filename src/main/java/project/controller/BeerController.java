package project.controller;



import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import project.persistence.entities.Beer;
import project.persistence.entities.Comment;
import project.persistence.entities.User;
import project.service.BeerService;
import project.service.CommentService;
import project.service.CustomUserDetailsService;

import javax.persistence.EntityManagerFactory;
import java.util.List;


@RestController
public class BeerController {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private BeerService beerService;
    private CustomUserDetailsService customUserDetailsService;
    private CommentService commentService;

    @Autowired
    public BeerController(BeerService beerService, CustomUserDetailsService customUserDetailsService, CommentService commentService){
        this.beerService = beerService;
        this.customUserDetailsService = customUserDetailsService;
        this.commentService = commentService;
    }

    @RequestMapping("/beers")
    public List<Beer> getBeers(){
    return beerService.findAll();
    }

    @RequestMapping("/beers/{beerId}")
    @ResponseBody
    public ObjectNode getBeer(@PathVariable Long beerId){
        Beer beer = beerService.findById(beerId);
        System.out.println(beer);
        return beer.getJSONBeer();
    }

    @RequestMapping(value="/comment/{username}/{beerId}/{title}/{comment}/{stars}")
    @ResponseBody
    public boolean comment(@PathVariable String username, @PathVariable Long beerId, @PathVariable String title, @PathVariable String comment, @PathVariable float stars){
        System.out.println("test");
        User currUser;
        try{
            currUser = customUserDetailsService.findByUsername(username);
            System.out.println("test");
            Beer currBeer = beerService.findById(beerId);
            Comment newComment = new Comment(currUser, currBeer, title, comment, stars);
            System.out.println(newComment);
            commentService.save(newComment);
            return true;
        } catch(Exception e){
            System.out.println(e);
            return false;
        }

        // todo gera ehv ef saveast ekki.......

    }


}
