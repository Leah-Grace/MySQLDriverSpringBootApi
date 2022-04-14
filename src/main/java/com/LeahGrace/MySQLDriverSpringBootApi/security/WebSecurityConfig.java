package com.LeahGrace.MySQLDriverSpringBootApi.security;

import com.LeahGrace.MySQLDriverSpringBootApi.security.jwt.AuthEntryPointJwt;
import com.LeahGrace.MySQLDriverSpringBootApi.security.jwt.AuthTokenFilter;
import com.LeahGrace.MySQLDriverSpringBootApi.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; //TODO: IS THIS CORRECT? - - 1:12:00 SPRINGBOOT 15
import org.springframework.security.crypto.password.PasswordEncoder;  //TODO: IS THIS CORRECT?
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService; //Implements the userDetailService we created. overrides the default

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler; //where unauthorized users are handled

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter(); //Runs a filter on all requests to all routes. Saves you from having to write a method/procedure on each route. This does checks on the data before it hits the route. The filter can be apart of the filterchain that manipoulates the data of every request before it hits the route.
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Autowired
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { //// .cors() adds global cross origin to all routes
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/auth/**").permitAll().and()
                .authorizeRequests().antMatchers("/api/test/**").permitAll()
                //.antMatchers("/api/geekout/*").permitAll()      // TODO: fix route to app-specific endpoint
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    } // this function is overriding the functionality of Spring Web.

}
