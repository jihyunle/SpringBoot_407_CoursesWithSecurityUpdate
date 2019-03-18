// this java class sets up the app to restrict access. by default, if access is not specified, it is denied.

package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
// above annots indicate to compiler that file is configuration file and that SpringSecurity is enabled for this app
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    // extended class has all the methods needed to include security in our app

    // the @Bean annot and passwordEncoder() method creates an obj that can be re-used to encode passwords in your app
    // provides an instance of BCryptPasswordEncoder, which is a pw encoder that uses the BCrypt hashing function
    @Bean
    public static BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // The configure() method configures users who can access the app.
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests() // tells your app which requests should be authorized
                .anyRequest().authenticated() // in this case, any request that is authenticated should be permitted
                .and() // adds addt'l rules
                .formLogin(); // tells spring app should show log in form
    }

    // multiple users can be configured here but atm it is for single in-memory user
    // you can further specify how users are granted access to app if their details are stored in db
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication().withUser("user")
                .password(passwordEncoder().encode("password"))
                .authorities("USER");
    }
}
