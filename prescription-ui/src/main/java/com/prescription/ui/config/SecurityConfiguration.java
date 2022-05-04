package com.prescription.ui.config;

import com.prescription.ui.filter.CustomerFilter;
import com.prescription.ui.util.UrlHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final UrlHelper urlHelper;

    private final CustomerFilter customerFilter;

    public SecurityConfiguration(UrlHelper urlHelper) {
        this.urlHelper = urlHelper;
        this.customerFilter = new CustomerFilter(this.urlHelper);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/static/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/**", "/static/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/authenticate/login").permitAll()
                .loginProcessingUrl("/authenticate/processLogin")
                .defaultSuccessUrl("/authenticate/dashboard")
                .failureUrl("/authenticate/login?error=true")
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/authenticate/logout"))
                .logoutSuccessUrl("/authenticate/login?logout");
        http.addFilterAfter(customerFilter, ChannelProcessingFilter.class);
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(3, TimeUnit.HOURS));
    }
}
