package muro.room.booking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class BasicAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.inMemoryAuthentication()
                .withUser("matt").password("{noop}secret").authorities("ROLE_ADMIN");
        // todo password should be encoded
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeRequests()
                .antMatchers(("/api/basicAuth/**")).hasRole("ADMIN")
                .and().httpBasic();
    }
}