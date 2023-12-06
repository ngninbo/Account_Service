package account.config;

import account.handler.AccountServiceAccessDeniedHandler;
import account.model.user.Role;
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
import static account.model.user.Role.*;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final int ENCODER_STRENGTH = 13;
    private final UserDetailsService userDetailsService;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private final AccountServiceAccessDeniedHandler accessDeniedHandler;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, RestAuthenticationEntryPoint entryPoint,
                          AccountServiceAccessDeniedHandler accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.restAuthenticationEntryPoint = entryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(getEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handle auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .mvcMatchers("/api/admin/**").hasRole(ADMINISTRATOR.name())
                .mvcMatchers("/api/acct/**").hasRole(ACCOUNTANT.name())
                .mvcMatchers(HttpMethod.POST, "/api/auth/changepass")
                .hasAnyRole(Role.getChangePassRoles())
                .mvcMatchers(HttpMethod.GET, "/api/empl/payment", "/api/empl/payment/*")
                .hasAnyRole(Role.getPaymentRoles())
                .mvcMatchers(HttpMethod.GET, "/api/security/events").hasRole(AUDITOR.name())
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
}
