package project.Config;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestSuite;
import org.json.JSONArray;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import project.persistence.entities.Beer;
import project.persistence.entities.Drinklist;
import project.persistence.repositories.BeerRepository;
import project.persistence.repositories.RoleRepository;
import project.persistence.entities.Role;
import project.persistence.entities.User;
import project.service.BeerService;
import project.service.CustomUserDetailsService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.json.JSONException;
import org.json.JSONObject;
import project.service.DrinklistService;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


/**
 * Class is only for setting up the Roles in the RoleRepository
 * Maybe delete later.... If we simply load these roles with sql directly
 */

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {


    private boolean alreadySetup = false;
    private RoleRepository roleRepository;
    private CustomUserDetailsService userService;
    private BeerService beerService;
    private DrinklistService drinklistService;

    @Autowired
    public InitialDataLoader(RoleRepository roleRepository, CustomUserDetailsService userService, BeerService beerService, DrinklistService drinklistService){
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.beerService = beerService;
        this.drinklistService = drinklistService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {


//        if (alreadySetup)
//            return;
        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");

        User hugi = new User("hugi", "hugi");
        User villi = new User("villi", "villi");
        User joe = new User("joe", "joe");




        try{
            userService.save(hugi);
        } catch(Exception e){
            System.out.println(e);
        }

        try{
            userService.save(villi);
        } catch(Exception e){
            System.out.println(e);
        }

        try{
            userService.save(joe);
        } catch(Exception e){
            System.out.println(e);
        }



        alreadySetup = true;



        String dir = System.getProperty("user.dir");
        String filename = dir + "/scraper/data.json";
//        System.out.println(filename);
        try {
            JSONArray jsonArray = parseJSONFile(filename);
//            System.out.println(jsonArray);
            insertBeersIntoDatabase(jsonArray);
        } catch (Exception e){
            System.out.println(e);
        }



        //create a drinklist for user

//        Long drinklistId = drinklistService.createDrinklist("hugi", "EasterBeers", true);
//
//        String[] allIds = {"24058", "23899", "25116", "18853", "21913", "23149"};
//        for(String id: allIds){
//            drinklistService.addToDrinklist("hugi", drinklistId, id);
//        }



    }

//    @Transactional
    private void createRoleIfNotFound(String name) {

        Role role = roleRepository.findByRole(name);
        if (role == null) {
            role = new Role(name);

            roleRepository.save(role);
        }
    }


    public static JSONArray parseJSONFile(String filename) throws JSONException, IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.ISO_8859_1);
        return new JSONArray(content);
    }


    public void insertBeersIntoDatabase(JSONArray allBeers){

        for(int i=0; i<allBeers.length(); i++){
            try{
//                System.out.println(allBeers.get(i));
                JSONObject object = allBeers.getJSONObject(i);
                Beer beer = beerService.findById(object.getString("product_number"));

                String volume = object.getString("volume");
                String alc = object.getString("alcohol");
                String price = object.getString("price");

                price = price.substring(0, price.length()-4);

                //eyda punktinum
                String newPrice = price;
                if(price.length() > 4){
                    newPrice = "";
                    for(int j=0; j<price.length();j++){
                        if(!price.substring(j,j+1).equals( ".")){
                            newPrice += price.substring(j,j+1);
                        }
                    }
                }

                String link = object.getString("link_to_vinbudin");
                String taste = object.getString("taste");
                String productNumber = object.getString("product_number");



                if(beer != null){
                    beer.setPrice(Integer.parseInt(newPrice));
                    beerService.save(beer);
                } else {
                    beer = new Beer();
                    beer.setName(object.getString("title"));

                    beer.setVolume((Integer.parseInt(volume.substring(0,volume.length()-3))));

                    beer.setAlcohol( Float.parseFloat(alc.substring(0, alc.length()-1)));


                    beer.setPrice(Integer.parseInt(newPrice));

                    beer.setLinkToVinbudin(link);

                    beer.setTaste(taste);
                    beer.setBeerId(productNumber);

                    beerService.save(beer);
                }
            } catch (Exception e){
                System.out.println(e);
            }
        }
    }
}
