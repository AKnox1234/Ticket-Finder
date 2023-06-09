package com.example.ticketfinder.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
References:
 https://dzone.com/articles/how-to-get-current-logged-in-username-in-spring-se
 https://howtodoinjava.com/spring-security/login-form-example/
 https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

    // inject dependencies
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     *
     * @return
     * processes authentication requests and returns an
     * authenticated object with full credentials
     */
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

    /**
     *
     * @param http
     * @return
     * @throws Exception
     * Controls access to the website
     * Controls the webpages that users, admins and not logged-in users can access
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/sign-in","/create-account" ,"/add-user").permitAll();
                    auth.requestMatchers("/admin", "/data-list-admin", "/edit-concert").hasAuthority("Admin");
                    auth.anyRequest().authenticated();

                    // if security is giving problems, comment out lines 49-51 and uncomment out line below
                    // this will stop authorization being needed to visit webpages
                    // auth.anyRequest().permitAll();

                })
                .formLogin()
                .loginPage("/sign-in")
                .loginProcessingUrl("/process-signIn")
                .successForwardUrl("/home")
                .failureUrl("/sign-in?error=true")
                .and()
                .logout()
                .logoutUrl("/sign-out")
                .and()
                .build();
    }
}
