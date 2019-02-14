package project.Config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import project.persistence.repositories.RoleRepository;
import project.persistence.entities.Role;
import project.persistence.entities.User;
import project.service.CustomUserDetailsService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * Class is only for setting up the Roles in the RoleRepository
 * Maybe delete later.... If we simply load these roles with sql directly
 */

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;
    private RoleRepository roleRepository;
    private CustomUserDetailsService userService;

    @Autowired
    public InitialDataLoader(RoleRepository roleRepository, CustomUserDetailsService userService){
        this.roleRepository = roleRepository;
        this.userService = userService;
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
    }

    @Transactional
    private void createRoleIfNotFound(String name) {

        Role role = roleRepository.findByRole(name);
        if (role == null) {
            role = new Role(name);

            roleRepository.save(role);
        }
    }



}
