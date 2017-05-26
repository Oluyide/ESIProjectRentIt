package com.example.common.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(securedEnabled=true)
public class SecurityConfiguration {
    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;

    @Autowired
    void configureAuthenticationSystem(AuthenticationManagerBuilder builder) throws Exception {
         builder
                 .jdbcAuthentication()
                 .dataSource(dataSource)
                 .usersByUsernameQuery(
                         "select username,password,enabled from users where username=?")
                 .authoritiesByUsernameQuery(
                     "select username,authority from authorities where username=?")
                 .and()
                 .inMemoryAuthentication()
                 .withUser("user2").password("user2").roles("USER2");

    }

    @Configuration
    class RestSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                    .antMatchers("/api/**").authenticated()
                    .and().httpBasic()
                    .authenticationEntryPoint((req,res,exc) ->
                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You don't have anything to see here"));
            http.headers().frameOptions().sameOrigin();
            //Thymeleaf dashboard
            http
                    .formLogin()
                    .loginPage("/dashboard/login.html")
                    .failureUrl("/dashboard/error.html")
                    .and()
                    .logout()
                    .logoutSuccessUrl("/dashboard/login.html");
            RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            http
                    .formLogin()
                    .loginPage("/login")
                    .successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                            Authentication cauthentication) throws IOException, ServletException {
                            redirectStrategy.sendRedirect(request, response, "/dashboard/catalog/form");
                        }
                    });
        }
    }
}
