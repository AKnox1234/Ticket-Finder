package com.example.ticketfinder.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        return http
                .authorizeHttpRequests(auth -> {
//                    auth.requestMatchers("/signIn","/createAccount" ,"/addUser").permitAll();
//                    auth.anyRequest().authenticated();
                    auth.anyRequest().permitAll();

                })
                .formLogin()
                .loginPage("/signIn")
                .loginProcessingUrl("/process-signIn")
                .successForwardUrl("/home")
                .failureUrl("/signIn?error=true")
                .and()
                .logout()
                .logoutUrl("/signOut")
                .and()
                .build();
    }

//    https://dzone.com/articles/how-to-get-current-logged-in-username-in-spring-se
//    https://howtodoinjava.com/spring-security/login-form-example/
//    https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java

}
