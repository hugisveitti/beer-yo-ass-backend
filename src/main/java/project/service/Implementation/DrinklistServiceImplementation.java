package project.service.Implementation;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.persistence.entities.Beer;
import project.persistence.entities.Drinklist;
import project.persistence.entities.User;
import project.persistence.repositories.BeerRepository;
import project.persistence.repositories.DrinklistRepository;
import project.persistence.repositories.UserRepository;
import project.service.DrinklistService;

import java.util.ArrayList;
import java.util.List;

@Service
public class DrinklistServiceImplementation implements DrinklistService {

    private DrinklistRepository drinklistRepository;
    private UserRepository userRepository;
    private BeerRepository beerRepository;

    @Autowired
    public DrinklistServiceImplementation(DrinklistRepository drinklistRepository, UserRepository userRepository, BeerRepository beerRepository){
        this.drinklistRepository = drinklistRepository;
        this.beerRepository = beerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ObjectNode> getAllPublicDrinklists(){
        List<ObjectNode> allPublicDrinklists = new ArrayList<>();

        List <Drinklist> drinklists = drinklistRepository.findPublicDrinklists();
        for(int i=0; i<drinklists.size(); i++){
            allPublicDrinklists.add(drinklists.get(i).getJSONDrinklist());
        }

        return allPublicDrinklists;
    }

    @Override
    public ObjectNode createDrinklist(String username, String name, boolean isPublic) {
        User user = userRepository.findByUsername(username);
        Drinklist drinklist = new Drinklist(name, isPublic);
        user.addDrinklist(drinklist);
        drinklistRepository.save(drinklist);
        return drinklist.getJSONDrinklist();
    }

    @Override
    public ObjectNode addToDrinklist(String username, Long drinklistId, String beerId){
        Drinklist drinklist = drinklistRepository.findByDrinklistId(drinklistId);
        Beer beer = beerRepository.findByBeerId(beerId);
        drinklist.addBeer(beer);

        drinklistRepository.save(drinklist);
        return drinklist.getJSONDrinklist();
    }

    @Override
    public ObjectNode cloneDrinklist(String username, Long drinklistId) {
        User user = userRepository.findByUsername(username);

        Drinklist drinklist = drinklistRepository.findByDrinklistId(drinklistId);
        List<Beer> uncheckedBeers = new ArrayList<>();
        List<Beer> checkedBeers = new ArrayList<>();
        for(int i=0; i<drinklist.getCheckedBeers().size(); i++){
            checkedBeers.add(drinklist.getCheckedBeers().get(i));
        }
        for(int i=0; i<drinklist.getUncheckedBeers().size(); i++){
            uncheckedBeers.add(drinklist.getUncheckedBeers().get(i));
        }
        Drinklist newDrinklist = new Drinklist(drinklist.getName(), checkedBeers, uncheckedBeers,drinklist.isPublic());

        user.addDrinklist(newDrinklist);
        drinklistRepository.save(newDrinklist);
        return newDrinklist.getJSONDrinklist();
    }

    @Override
    public ObjectNode markBeerOnDrinklist(String username, Long drinklistId, String beerId, boolean marked) {
        Drinklist drinklist = drinklistRepository.findByDrinklistId(drinklistId);
        Beer beer = beerRepository.findByBeerId(beerId);

        if(marked){
            drinklist.checkBeer(beer);
        } else {
            drinklist.uncheckBeer(beer);
        }

        drinklistRepository.save(drinklist);
        return drinklist.getJSONDrinklist();
    }

    @Override
    public  List<ObjectNode> getMyDrinklists(String username){
        User user = userRepository.findByUsername(username);
        return user.getObjectNodeMyDrinklist();
    }

    @Override
    public void deleteDrinklist(String username, Long drinklistId) {
        Drinklist drinklist = drinklistRepository.findByDrinklistId(drinklistId);
        User user = userRepository.findByUsername(username);
        user.removeDrinklist(drinklist);
        drinklistRepository.delete(drinklist);

    }
}
