package project.Config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;


/**
 * Class that controls authentication and
 * what pages who can view.
 */


//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private DataSource dataSource;

    @Autowired
    public WebSecurityConfig(DataSource dataSource){
        this.dataSource = dataSource;
    }


    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from user_account where username=?")
                .authoritiesByUsernameQuery("select username, role from user_role where username=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests().antMatchers("/","/signup","/logout").permitAll()
                .antMatchers("/sendbet","/userpage").hasRole("USER")
                .and()
                .formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/userpage").failureUrl("/login?error=true")
                .and()
                .logout().deleteCookies("remove").invalidateHttpSession(true).logoutSuccessUrl("/").permitAll();


        //virkar ekki
        //http.exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());
//        http.exceptionHandling().accessDeniedPage("/403");

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}