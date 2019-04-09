package project.service.Implementation;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.persistence.entities.Beer;
import project.persistence.entities.Role;
import project.persistence.repositories.RoleRepository;
import project.persistence.entities.User;
import project.persistence.repositories.UserRepository;

import org.springframework.security.core.AuthenticationException;
import project.service.BeerService;
import project.service.CustomUserDetailsService;

import java.util.*;

@Service
public class CustomUserDetailsServiceImplementation implements CustomUserDetailsService {

    private RoleRepository roleRepository;
    private UserRepository repository;
    private PasswordEncoder passwordEncoder;
    private BeerService beerService;

    // Dependency Injection
    @Autowired
    public CustomUserDetailsServiceImplementation(UserRepository repository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, BeerService beerService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.beerService = beerService;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = repository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        return user;
    }


    //Paeling ad setja thetta i ser classa
    //Exception sent if there exsitis a user with this username
    public class UserExsitsException extends AuthenticationException {
        public UserExsitsException(final String msg){
            super(msg);
        }
    }

    public User save(User user) throws UserExsitsException {
        // Save the user that we received from the form
        Role role = roleRepository.findByRole("ROLE_USER");
        Set<Role> newRoles = new HashSet<>();
        newRoles.add(role);
        user.setRoles(newRoles);
        user.setEnabled(true);
        //check if username is already in database
        if(repository.findByUsername(user.getUsername()) != null){
            throw new UserExsitsException("Username taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public void delete(User user) {
        repository.delete(user);
    }


    public List<User> findAll() {
        return repository.findAll();
    }


    public boolean login(String username, String password){
        User user = repository.findByUsername(username);
        if(user == null){
            return false;
        }
        boolean mat = passwordEncoder.matches(password, user.getPassword());
        return mat;
    }

    @Override
    public User findOne(Long id) {
        return repository.findOne(id);
    }


    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }



    public boolean addToMyBeers(String username, String beerId){
        try{
            User user = findByUsername(username);
            System.out.println(beerId);
            Beer beer = beerService.findById(beerId);
            user.addToMyBeers(beer);
            repository.save(user);
            return true;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    @Override
    public boolean removeFromMyBeers(String username, String beerId) {
        try{
            User user = findByUsername(username);
            Beer beer = beerService.findById(beerId);
            user.removeFromMyBeers(beer);
            repository.save(user);
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }


    @Override
    public boolean changeProfilePicture(String username, String beerId) {
        User user = repository.findByUsername(username);
        user.setProfilePicture(beerId);
        repository.save(user);
        return true;
    }

    @Override
    public boolean newGameScore(String username, int score) {

        try{
            User user = repository.findByUsername(username);
            user.setGameScore(score);
            repository.save(user);
            return true;
        } catch(Exception e){
            System.out.println(e);
            return false;
        }
    }



    // use ObjectNode to store gameScore, and username, dont know how to query and get objectNode
    @Override
    public List<ObjectNode> getAllGameScores() {
        List<ObjectNode> jsonArr = new ArrayList<>();
        List<User> allUsers = findAll();
        for(int i=0; i<allUsers.size(); i++){
            jsonArr.add(allUsers.get(i).getJsonScore());
        }
        return jsonArr;
    }
}
