package project.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import project.persistence.entities.Drinklist;

import java.util.List;

public interface DrinklistService {


    List<ObjectNode> getAllPublicDrinklists();

    ObjectNode createDrinklist(String username, String name, boolean isPublic);

    ObjectNode addToDrinklist(String username, Long drinklistId, String beerId);

    ObjectNode cloneDrinklist(String username, Long drinklistId);

    ObjectNode markBeerOnDrinklist(String username, Long drinklistId, String beerId, boolean marked);

    List<ObjectNode> getMyDrinklists(String username);

    void deleteDrinklist(String username, Long drinklistId);

}
