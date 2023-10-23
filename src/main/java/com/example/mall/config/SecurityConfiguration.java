package com.example.mall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.example.mall.service.MemberService;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfiguration  {

    @Autowired
    MemberService memberService;

    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector).servletPath("/");

        // http.exceptionHandling(ex -> ex
        // .authenticationEntryPoint
        // (new CustomAuthenticationEntryPoint()));

        http.authorizeHttpRequests(authz  -> authz
                        .requestMatchers(mvcMatcherBuilder.pattern("")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET,"members/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST,"members/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("images/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("admin/**")).hasRole("ADMIN")
                        .anyRequest().permitAll());
                
        http.formLogin(form -> form
                .permitAll()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                )
            .logout(authz -> authz
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/"));
        
        
                                                
        
        

        return http.build();
    }

    @Bean
    AuthenticationManager auth(AuthenticationConfiguration authconfig) throws Exception{
        return authconfig.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        
        return (web) -> web.ignoring().requestMatchers(
            new AntPathRequestMatcher("/css/"),
            new AntPathRequestMatcher("/js/**"),
            new AntPathRequestMatcher("/img/**")
            );
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }


}
