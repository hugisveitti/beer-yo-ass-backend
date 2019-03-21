package project.service.Implementation;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.persistence.entities.Beer;
import project.persistence.repositories.BeerRepository;
import project.service.BeerService;

import java.util.ArrayList;
import java.util.List;

@Service
public class BeerServiceImplementation implements BeerService {

    private BeerRepository beerRepository;

    @Autowired
    public BeerServiceImplementation(BeerRepository beerRepository){
        this.beerRepository = beerRepository;
    }


    //we don't want the comments when fetching all the beers.
    public List<ObjectNode> findAll(){
        List<Beer> allBeers = beerRepository.findAll();
        List<ObjectNode> returnBeers = new ArrayList<>();
        for(int i=0; i<allBeers.size(); i++){
            returnBeers.add(allBeers.get(i).getJSONBeer(false));
        }
        return returnBeers;
    }

    public Beer findByName(String name){
        return beerRepository.findByName(name);
    }


    public Beer findById(String id){
        return beerRepository.findByBeerId(id);
    }


    public Beer save(Beer beer){
        return beerRepository.save(beer);
    }
}
