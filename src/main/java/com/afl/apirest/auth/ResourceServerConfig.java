package com.afl.apirest.auth;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/clientes", "/api/clientes/page/**","/api/uploads/img/**", "/images/**").permitAll()
				// para desarrollo
				/*
				 * .antMatchers(HttpMethod.GET, "/api/clientes/{id}").permitAll()
				 * .antMatchers(HttpMethod.DELETE, "/api/facturas/{id}").permitAll()
				 * .antMatchers(HttpMethod.GET, "/api/facturas/**").permitAll()
				 * .antMatchers(HttpMethod.POST, "/api/facturas/**").permitAll()
				 */

					
		        // para desarrollo
		         
		        /* Pasamos la seguridad a SpringSecurityConfig incorporando la anotacion @EnableGlobalMethodSecurity
				 *  y en el controlador, una anotación por cada url que se necesite controlar, esta segunda opción parece más sencilla e intuitiva
				 *
				 * .antMatchers(HttpMethod.GET, "/api/clientes/{id}").hasAnyRole("USER", "ADMIN") 
				 * .antMatchers(HttpMethod.POST, "/api/clientes/upload").hasAnyRole("USER", "ADMIN")
				 * .antMatchers(HttpMethod.POST, "/api/clientes").hasRole("ADMIN")
				 * .antMatchers("/api/clientes/**").hasRole("ADMIN")
				 */
		.anyRequest().authenticated()
		.and().cors().configurationSource(corsConfigurationSource());
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource () {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8070", "*"));
		config.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter () {
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter> (new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}
	
}
