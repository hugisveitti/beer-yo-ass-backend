package project.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.persistence.entities.User;
import project.service.BeerService;
import project.service.CustomUserDetailsService;

import java.util.List;


/**
 * Controller for most post and get request that have something to do with the user
 * note that WebSecurityConfig handles the login post because of authentication
 */

@RestController
public class UserController {

    private CustomUserDetailsService customUserDetailsService;
    private BeerService beerService;
    // Dependency Injection

    @Autowired
    public UserController(CustomUserDetailsService customUserDetailsService, BeerService beerService) {
        this.customUserDetailsService = customUserDetailsService;
        this.beerService = beerService;
    }

//    @PostMapping(value = "/login")
    @RequestMapping(value="/login/{username}/{password}")
    @ResponseBody
    public boolean login(@PathVariable String username, @PathVariable String password){
        System.out.println(username + " " + password);

        //ekki secure!!!
        return customUserDetailsService.login(username, password);
    }


    @RequestMapping(value="/signup/{username}/{password}")
    @ResponseBody
    public boolean signup(@PathVariable String username, @PathVariable String password){
        System.out.println(username + " " + password);

        User user = new User(username, password);
        try{
            customUserDetailsService.save(user);
            return true;
        }catch (Exception e) {
            System.out.println(e);
            //ef return false tha er username fratekid...
            return false;
        }
    }


    @RequestMapping(value="/addToMyBeers/{username}/{beerId}")
    @ResponseBody
    public boolean addToMyBeers(@PathVariable String username, @PathVariable String beerId){
        customUserDetailsService.addToMyBeers(username, beerService.findById(beerId));
        return true;
    }



    @RequestMapping(value="/myBeers/{username}")
    @ResponseBody
    public List<ObjectNode> myBeers(@PathVariable String username){
        return customUserDetailsService.findByUsername(username).getObjectNodeMyBeers();
    }
}
