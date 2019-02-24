package project.controller;



import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project.persistence.entities.Beer;
import project.service.BeerService;

import javax.persistence.EntityManagerFactory;
import java.util.List;


@RestController
public class BeerController {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private BeerService beerService;

    @Autowired
    public BeerController(BeerService beerService){
        this.beerService = beerService;
    }

    @RequestMapping("/beers")
    public List<Beer> getBeers(){
//        JSONObject jA = (JSONArray)JSONSerializer.toJSON(beerService.findAll());

//        JSONArray jA = new JSONArray();
//        List<Beer> allBeers = beerService.findAll();
//        for(int i=0; i<allBeers.size(); i++){
//            JSONObject jb = new JSONObject();
//
//            try{
//                jb.put("beer", allBeers.get(i));
//                jA.put(jb);
//            } catch (Exception e){
//                System.out.println(e);
//            }
//
//        }
//
//        return jA;
    return beerService.findAll();
    }
}
