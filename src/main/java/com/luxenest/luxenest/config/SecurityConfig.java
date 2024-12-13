package com.luxenest.luxenest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.luxenest.luxenest.service.MyUserDetailsService;;

@Configuration
public class SecurityConfig {

   @Autowired
   private MyUserDetailsService userDetailsService;

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http
            .csrf(Customizer -> Customizer.disable())
            .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/login", "/signup").permitAll()
                  .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
   }

   // old version
   // http.authorizeRequests()
   // .antMatchers("/journal/**", "/user/**").authenticated() use matra access
   // garna pauxa
   // .antMatchers("/admin/**").hasRole("ADMIN") yo admin la matra
   // .anyRequest().permitAll(); this is for all request
   // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable();
   // http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder(12);
   }

   @Bean
   AuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
      provider.setUserDetailsService(userDetailsService);
      provider.setPasswordEncoder(passwordEncoder());
      return provider;
   }

     @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }

}
