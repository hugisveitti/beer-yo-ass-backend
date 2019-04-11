package project.controller;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project.service.DrinklistService;

import java.util.List;

@RestController
public class DrinklistController {

    private DrinklistService drinklistService;

    @Autowired
    public DrinklistController(DrinklistService drinklistService){
        this.drinklistService = drinklistService;
    }


    @RequestMapping("/getMyDrinklists/{username}")
    @ResponseBody
    public List<ObjectNode> getMyDrinklists(@PathVariable String username){ //get request
        return drinklistService.getMyDrinklists(username);
    }

    @RequestMapping("/getAllPublicDrinklists")
    public List<ObjectNode> getAllPublicDrinklists(){ //get request
        return drinklistService.getAllPublicDrinklists();
    }

    @RequestMapping("/addToDrinklist/{username}/{drinklistId}/{beerId}")
    public ObjectNode addToDrinklist(@PathVariable String username, @PathVariable Long drinklistId, @PathVariable String beerId){
        return drinklistService.addToDrinklist(username, drinklistId,beerId);
    }


    //post request
    @RequestMapping("/createDrinklist/{username}/{name}/{isPublic}")
    @ResponseBody
    public Long createDrinklist(@PathVariable String username, @PathVariable String name, @PathVariable boolean isPublic){
        return drinklistService.createDrinklist(username, name, isPublic);

    }

    //post request
    @RequestMapping("/cloneDrinklist/{username}/{drinklistId}")
    public boolean cloneDrinklist(@PathVariable String username, @PathVariable Long drinklistId){
        drinklistService.cloneDrinklist(username, drinklistId);
        return true;
    }


    @RequestMapping("/markBeerOnDrinklist/{username}/{drinklistId}/{beerId}/{marked}")
    @ResponseBody
    public boolean markBeerOnDrinklist(@PathVariable String username, @PathVariable Long drinklistId, @PathVariable String beerId, @PathVariable boolean marked){
        drinklistService.markBeerOnDrinklist(username,drinklistId,beerId,marked);
        return true;
    }


    @RequestMapping("/deleteDrinklist/{username}/{drinklistId}")
    @ResponseBody
    public boolean deleteDrinklist(@PathVariable String username, @PathVariable Long drinklistId){
        drinklistService.deleteDrinklist(username, drinklistId);
        return true;
    }
}
