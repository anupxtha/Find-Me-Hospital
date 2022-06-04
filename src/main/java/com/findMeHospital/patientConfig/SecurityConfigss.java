package com.findMeHospital.patientConfig;

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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@Order(3)
public class SecurityConfigss extends WebSecurityConfigurerAdapter {
	
	@Bean
	public AuthenticationSuccessHandler successHandler() {
	    return new MyCustomLoginSuccessHandler("/patient");
	}
	
    @Bean
    public UserDetailsService getPatientDetailService() {
        return new PatientDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoderPatient() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProviderPatient() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(this.getPatientDetailService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoderPatient());

        return daoAuthenticationProvider;
    }

    // Configure Method
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProviderPatient());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	http.requestMatchers().antMatchers("/patient/**")
        .and()
        .authorizeRequests()
        .antMatchers("/patient/**").hasRole("PATIENT")
        .and().formLogin().loginPage("/patient/signin")
		.successHandler(successHandler())
	.permitAll()
	.and().logout().logoutUrl("/patient/logout").logoutSuccessUrl("/patient/signin");
    	http.csrf().disable();
    	
    	
//      http.antMatcher("/patient/**")
//		.authorizeRequests().anyRequest().authenticated()
//		.and().formLogin().loginPage("/patient/signin")
//			.defaultSuccessUrl("/patient", true)
//		.permitAll()
//		.and().logout().logoutUrl("/patient/logout").logoutSuccessUrl("/patient/signin");
//	
//	http.csrf().disable();
    }

}
