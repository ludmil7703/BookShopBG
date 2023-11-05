package org.softuni.bookshopbg.config;

import lombok.Value;
import org.softuni.bookshopbg.model.enums.UserRoleEnum;
import org.softuni.bookshopbg.repositories.UserRepository;
import org.softuni.bookshopbg.service.impl.BookStoreUserDetailsService;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

//  private final String rememberMeKey;

//  public SecurityConfiguration(@Value("${mobilele.remember.me.key}")
//    String rememberMeKey) {
//    this.rememberMeKey = rememberMeKey;
//  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity.authorizeHttpRequests(
        // Define which urls are visible by which users
        authorizeRequests -> authorizeRequests
            // All static resources which are situated in js, images, css are available for anyone
            .requestMatchers("/css/**").permitAll()
            .requestMatchers("/images/**").permitAll()
            .requestMatchers("/js/**").permitAll()
            // Allow anyone to see the home page, the registration page and the login form
            .requestMatchers("/", "/users/login", "/users/register", "/users/login-error").permitAll()
            .requestMatchers("/offers/all").permitAll()
            .requestMatchers(HttpMethod.GET, "/offer/**").permitAll()
            .requestMatchers("/error").permitAll()
            .requestMatchers("/books/**").hasRole(UserRoleEnum.ADMIN.name())
            // all other requests are authenticated.
            .anyRequest().authenticated()
    ).formLogin(
        formLogin -> {
          formLogin
              // redirect here when we access something which is not allowed.
              // also this is the page where we perform login.
              .loginPage("/users/login")
              // The names of the input fields (in our case in auth-login.html)
              .usernameParameter("username")
              .passwordParameter("password")
              .defaultSuccessUrl("/")
              .failureForwardUrl("/users/login-error");
        }
    ).logout(
        logout -> {
          logout
              // the URL where we should POST something in order to perform the logout
              .logoutUrl("/users/logout")
              // where to go when logged out?
              .logoutSuccessUrl("/")
              // invalidate the HTTP session
              .invalidateHttpSession(true);
        }
//    ).rememberMe(
//        rememberMe -> {
//          rememberMe
//              .key(rememberMeKey)
//              .rememberMeParameter("rememberme")
//              .rememberMeCookieName("rememberme");
//        }
    ).build();
  }

  @Bean
  public UserDetailsService userDetailsService(UserRepository userRepository) {
    // This service translates the mobilele users and roles
    // to representation which spring security understands.
    return new BookStoreUserDetailsService(userRepository);
  }

}
