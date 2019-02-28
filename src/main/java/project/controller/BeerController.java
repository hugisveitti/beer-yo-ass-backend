package project.controller;



import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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
    public Beer getBeer(@PathVariable Long beerId){
        return beerService.findById(beerId);
    }

    @RequestMapping("/comment/{username}/{beerId}/{title}/{comment}/{stars}/")
    @ResponseBody
    public boolean comment(@PathVariable String username, @PathVariable Long beerId, @PathVariable String title, @PathVariable String comment, @PathVariable float stars){
        User currUser = customUserDetailsService.findByUsername(username);
        Beer currBeer = beerService.findById(beerId);
        Comment newComment = new Comment(currUser, currBeer, title, comment, stars);
        commentService.save(newComment);
        // todo gera ehv ef saveast ekki.......
        return true;
    }




}
