package jp.co.sogeninc.semv2_be.config;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import jp.co.sogeninc.semv2_be.domain.User;
import jp.co.sogeninc.semv2_be.service.UserService;
import lombok.Setter;

@EnableOAuth2Sso
@EnableGlobalMethodSecurity(prePostEnabled=true, securedEnabled=true)
@EnableWebSecurity
@Configuration
@ConfigurationProperties(prefix="semv2")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Setter
	private String frontendUrl;
	@Setter
	private String adminPassword;

    @Autowired
    private UserService userService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		
		.antMatchers("/", "/login")
		//.antMatchers("/", "/login", "/api/admin/login") //for debug
		
		.permitAll()
		.anyRequest()
		.authenticated()
		.and().logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl(frontendUrl)
		.and()
		.csrf().disable(); //debug
	}

	@Bean // google oauth用
	public PrincipalExtractor principalExtractor() {

		return map -> {
			User l = userService.findByEmail(map.get("email").toString());
			if(l == null) {
				l = new User();
				l.setUid("stranger");
				l.setPassword("stranger");
				l.getRoles().add(User.Role.STRANGER);
			}
//			System.out.println(l);
			return l;
		};
	}

	@Bean
	public AuthoritiesExtractor authoritiesExtractor() {
		return map -> {
			User l = userService.findByEmail(map.get("email").toString());
			if(l == null) {
				l = new User();
				l.setUid("stranger");
				l.setPassword("stranger");
				l.getRoles().add(User.Role.STRANGER);
			}
			return new ArrayList<GrantedAuthority>(l.getAuthorities());
		};
}

}
