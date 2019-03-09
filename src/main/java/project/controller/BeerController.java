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


/**
 * REST controller.
 * Handles returning all the beers, specific beers and adding comments to beers.
 *
 * TODO change requestmapping to appropriate post og get requests.
 */

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


    //ObjectNode implements JSONObject
    @RequestMapping("/beers")
    public List<ObjectNode> getBeers(){
    return beerService.findAll();
    }

    @RequestMapping("/beers/{beerId}")
    @ResponseBody
    public ObjectNode getBeer(@PathVariable String beerId){
        Beer beer = beerService.findById(beerId);
        System.out.println(beer);
        //maybe move this to the beerserivce.
        return beer.getJSONBeer(true);
    }

    @RequestMapping(value="/comment/{username}/{beerId}/{title}/{comment}/{stars}")
    @ResponseBody
    public boolean comment(@PathVariable String username, @PathVariable String beerId, @PathVariable String title, @PathVariable String comment, @PathVariable float stars){
        System.out.println("test");
        User currUser;
        try{
            currUser = customUserDetailsService.findByUsername(username);
            Beer currBeer = beerService.findById(beerId);
            Comment newComment = new Comment(currUser, currBeer, title, comment, stars);
            commentService.save(newComment);
            return true;
        } catch(Exception e){
            System.out.println(e);
            return false;
        }

        // todo gera ehv ef saveast ekki.......
    }


    //not sure how to these return types should be...
    @RequestMapping(value="/deleteComment/{commentId}")
    @ResponseBody
    public boolean deleteComment(@PathVariable Long commentId){
        commentService.delete(commentId);
        return true;
    }


    @RequestMapping(value="/updateComment/{commentId}/{title}/{comment}/{stars}")
    @ResponseBody
    public boolean updateComment(@PathVariable Long commentId, @PathVariable String title, @PathVariable String comment, @PathVariable float stars){
        commentService.update(commentId, title, comment, stars);
        return true;
    }



}
