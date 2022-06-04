package com.findMeHospital.adminConfig;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Order(1)
public class SecurityConfigs extends WebSecurityConfigurerAdapter {
    @Bean
    public UserDetailsService getAdminDetailService() {
        return new AdminDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoderAdmin() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProviderAdmin() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(this.getAdminDetailService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoderAdmin());

        return daoAuthenticationProvider;
    }

    // Configure Method
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProviderAdmin());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	http.requestMatchers().antMatchers("/admin/**")
        .and()
        .authorizeRequests()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .and().formLogin().loginPage("/admin/login")
		.defaultSuccessUrl("/admin", true)
	.permitAll()
	.and().logout().logoutUrl("/admin/logout").logoutSuccessUrl("/admin/login");
    	http.csrf().disable();
    	
//      http.antMatcher("/admin/**")
//		.authorizeRequests().anyRequest().authenticated()
//		.and().formLogin().loginPage("/admin/login")
//			.defaultSuccessUrl("/admin", true)
//		.permitAll()
//		.and().logout().logoutUrl("/admin/logout").logoutSuccessUrl("/admin/login");
//	
//	http.csrf().disable();

//	.failureUrl("/accessdenied")
    }
}
