package account.util.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final int ENCODER_STRENGTH = 13;
    private final UserDetailsService userDetailsService;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, RestAuthenticationEntryPoint entryPoint) {
        this.userDetailsService = userDetailsService;
        this.restAuthenticationEntryPoint = entryPoint;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(getEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and()
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handle auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .mvcMatchers("/api/admin/**").hasRole("ADMINISTRATOR")
                .mvcMatchers("/api/acct/**").hasRole("ACCOUNTANT")
                .mvcMatchers(HttpMethod.POST, "/api/auth/changepass").hasAnyRole("USER", "ACCOUNTANT", "ADMINISTRATOR")
                .mvcMatchers(HttpMethod.GET, "/api/empl/payment", "/api/empl/payment/*").hasAnyRole("USER", "ACCOUNTANT")
                .antMatchers(HttpMethod.POST, "/api/signup").permitAll()
                // other matchers
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }



    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(ENCODER_STRENGTH);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }
}
