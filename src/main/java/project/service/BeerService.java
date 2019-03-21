package project.service;


import com.fasterxml.jackson.databind.node.ObjectNode;
import project.persistence.entities.Beer;

import java.util.List;


public interface BeerService {

    /**
     * Get all {@link Beer}s
     * @return A list of {@link Beer}s
     */
    List<ObjectNode> findAll();


    /**
     * Find a {@link Beer} based on {@link String name}
     * @param name {@link String name} to be found
     * @return a {@link Beer} with name {@link String name}
     */
    Beer findByName(String name);

    /**
     * Find a {@link Beer} based on {@link String id}
     * @return a {@link Beer} with the id {@link String id}
     */
    Beer findById(String id);

    Beer save(Beer beer);

}
