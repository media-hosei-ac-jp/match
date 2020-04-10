package jp.co.sogeninc.semv2_be.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.Setter;

@Service
public class AuthService {
    
	@Autowired
	private AuthenticationManager authManager;

	/**
	 * ログイン処理を行います.
	 * @return
	 */
	public boolean login(String id, String password) {
		//Spring Security認証処理
		Authentication authResult = null;
		try{
			Authentication request = new UsernamePasswordAuthenticationToken(id, password);
			authResult = authManager.authenticate(request);
			SecurityContextHolder.getContext().setAuthentication(authResult);
			
			return authResult.isAuthenticated();
		}
		catch(Exception e) {
			//e.printStackTrace();
			return false;
		}
	}
}