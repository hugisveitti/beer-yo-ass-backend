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
import project.persistence.repositories.RoleRepository;
import project.persistence.entities.Role;
import project.persistence.entities.User;
import project.service.BeerService;
import project.service.CustomUserDetailsService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;


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

    @Autowired
    public InitialDataLoader(RoleRepository roleRepository, CustomUserDetailsService userService, BeerService beerService){
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.beerService = beerService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {


        if (alreadySetup)
            return;
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
        System.out.println(filename);
        try {
            JSONArray jsonArray = parseJSONFile(filename);
            System.out.println(jsonArray);
            insertBeersIntoDatabase(jsonArray);
        } catch (Exception e){
            System.out.println(e);
        }

    }

    @Transactional
    private void createRoleIfNotFound(String name) {

        Role role = roleRepository.findByRole(name);
        if (role == null) {
            role = new Role(name);

            roleRepository.save(role);
        }
    }


    public static JSONArray parseJSONFile(String filename) throws JSONException, IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);


        File file = new File(filename);
        ObjectMapper mapper = new ObjectMapper();

//        JsonNode masterJSON = mapper.readTree(file);
//        System.out.println(masterJSON);
        System.out.println("#########");
        String str;
    try{
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), "UTF8"));

        while ((str = in.readLine()) != null) {
            System.out.println(str);
        }

        in.close();
        return new JSONArray(str);
    }
	    catch (UnsupportedEncodingException e)
    {
        System.out.println(e.getMessage());
    }
	    catch (IOException e)
    {
        System.out.println(e.getMessage());
    }
	    catch (Exception e)
    {
        System.out.println(e.getMessage());
    }


        System.out.println("###################");
        return new JSONArray(content);

    }


    public void insertBeersIntoDatabase(JSONArray allBeers){

        for(int i=0; i<allBeers.length(); i++){
            try{
                System.out.println(allBeers.get(i));
                JSONObject object = allBeers.getJSONObject(i);
                Beer beer = new Beer();

                beer.setName(object.getString("title"));

                String volume = object.getString("volume");
                beer.setVolume((Integer.parseInt(volume.substring(0,volume.length()-3))));

                String alc = object.getString("alcohol");
                beer.setAlcohol( Float.parseFloat(alc.substring(0, alc.length()-1)));

                String price = object.getString("price");
                price = price.substring(0, price.length()-4);

                //eyda punktinum
                String newPrice = price;
                if(price.length() > 4){
                    for(int j=0; j<price.length();j++){
                        if(price.substring(i) != "."){
                            newPrice += price.substring(i);
                        }
                    }
                }

                beer.setPrice(Integer.parseInt(newPrice));

                String link = object.getString("link_to_vinbudin");
                beer.setLinkToVinbudin(link);

                String taste = object.getString("taste");
                beer.setTaste(taste);

                String productNumber = object.getString("product_number");
                beer.setBeerId(productNumber);

                System.out.println(newPrice + " " + alc+ " " +  volume);
                beerService.save(beer);


                System.out.println(object.getString("title"));
            } catch (Exception e){
                System.out.println(e);
            }

        }
    }
}
