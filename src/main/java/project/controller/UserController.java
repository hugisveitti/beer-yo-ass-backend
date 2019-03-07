package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.persistence.entities.User;
import project.service.CustomUserDetailsService;



/**
 * Controller for most post and get request that have something to do with the user
 * note that WebSecurityConfig handles the login post because of authentication
 */

@RestController
public class UserController {

    private CustomUserDetailsService customUserDetailsService;
    // Dependency Injection

    @Autowired
    public UserController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
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
}
