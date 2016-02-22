package opensource.onlinestore.configuration;

import opensource.onlinestore.Utils.PasswordUtil;
import opensource.onlinestore.filters.AuthenticationFilter;
import opensource.onlinestore.filters.LoginFilter;
import opensource.onlinestore.service.authentication.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;


/**
 * Created by maks(avto12@i.ua) on 27.01.2016.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource(name="userService")
    private UserDetailsService userService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    TokenAuthenticationService tokenAuthenticationService;
    @Autowired
    AuthenticationFilter authenticationFilter;

    @Bean
    public LoginFilter loginFilter() throws Exception {
        return new LoginFilter("/login", authenticationManagerBean());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .anonymous()
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/current").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest()
                .authenticated()
                .and().addFilterBefore(loginFilter(),
                UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);
        
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .eraseCredentials(true)
                .userDetailsService(userService)
                .passwordEncoder(PasswordUtil.getPasswordEncoder());
    }




}
