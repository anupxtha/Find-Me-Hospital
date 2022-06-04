package com.findMeHospital.hospitalConfig;

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
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public UserDetailsService getHospitalDetailService() {
        return new HospitalDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProviderHospital() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(this.getHospitalDetailService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    // Configure Method
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProviderHospital());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	http.requestMatchers().antMatchers("/hospital/**")
        .and()
        .authorizeRequests()
        .antMatchers("/hospital/**").hasRole("HOSPITAL")
        .and().formLogin().loginPage("/hospital/login")
		.defaultSuccessUrl("/hospital", true)
	.permitAll()
	.and().logout().logoutUrl("/hospital/logout").logoutSuccessUrl("/hospital/login");
    	http.csrf().disable();
    	
//      http.antMatcher("/hospital/**")
//		.authorizeRequests().anyRequest().authenticated()
//		.and().formLogin().loginPage("/hospital/login")
//			.defaultSuccessUrl("/hospital", true)
//		.permitAll()
//		.and().logout().logoutUrl("/hospital/logout").logoutSuccessUrl("/hospital/login");
//	
//	http.csrf().disable();
  	
    }
    
    
    
//    .failureUrl("/accessdenied")

}
