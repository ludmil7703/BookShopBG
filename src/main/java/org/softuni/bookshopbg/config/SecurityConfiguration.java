package org.softuni.bookshopbg.config;

import org.softuni.bookshopbg.model.enums.UserRoleEnum;
import org.softuni.bookshopbg.repositories.UserRepository;
import org.softuni.bookshopbg.service.impl.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;

@Configuration
public class SecurityConfiguration {

    private final String[] PUBLIC_MATCHERS = new String[]{
            "/css/**",
            "/js/**",
            "/images/**",
            "/bookshelf",
            "/forgetPassword",
            "/login",
            "/fonts/**",
            "/searchBook",
            "/searchByCategory",
            "/search",
            "/bookDetail/**",
            "/faq",
            "/contact",
            "/error",
            "/users/register",
            "/users/login",
            "/users/login-error",
            "/"

    };
//  private final String rememberMeKey;
//
//  public SecurityConfiguration(@Value("${mobilele.remember.me.key}")
//    String rememberMeKey) {
//    this.rememberMeKey = rememberMeKey;
//  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

      return httpSecurity.authorizeHttpRequests(
        authorizeRequests -> authorizeRequests
                .requestMatchers(PUBLIC_MATCHERS).permitAll()
            .requestMatchers("/books/**").hasRole(UserRoleEnum.ADMIN.name())
            .anyRequest().authenticated()
    )
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(
        formLogin -> {
          formLogin
              .loginPage("/users/login")
              .usernameParameter("username")
              .passwordParameter("password")
              .defaultSuccessUrl("/")
              .failureForwardUrl("/users/login-error");
        }
    ).logout(
        logout -> {
          logout
              .logoutUrl("/users/logout")
              .logoutSuccessUrl("/")
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
    return new UserDetailServiceImpl(userRepository);
  }

}
