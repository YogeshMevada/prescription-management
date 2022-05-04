package com.prescription.management.config;

import com.prescription.management.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final UserDetailsService userDetailsService;

    private final JwtRequestFilter jwtRequestFilter;

    private final String allowedDomains;

    @Autowired
    public SecurityConfiguration(final UserDetailsService userDetailsService,
                                 final JwtRequestFilter jwtRequestFilter,
                                 @Value("${service.domain.allowed}") final String allowedDomains) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.allowedDomains = allowedDomains;
    }

    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/**/authenticate", "/api/**/register", "/authenticate/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(@Value("${service.domain.allowed}") final String allowedDomains) {
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedOrigins(Arrays.asList(allowedDomains));
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:8081"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setMaxAge(3600l);
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedHeader("*");
        final UrlBasedCorsConfigurationSource cors = new UrlBasedCorsConfigurationSource();
        cors.registerCorsConfiguration("/**", corsConfiguration);
        return cors;
    }

    /*@Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins(allowedDomains)
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .maxAge(3600l)
                .allowCredentials(true)
                .exposedHeaders("Authorization");
//                .allowedHeaders("*");
    }*/
}
