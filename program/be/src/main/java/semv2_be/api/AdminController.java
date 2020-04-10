package jp.co.sogeninc.semv2_be.api;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sogeninc.semv2_be.service.AuthService;
import lombok.Setter;

/**
 * Admin Restコントローラー
 *
 */
@ConfigurationProperties(prefix="semv2")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
	@Setter
	private String adminPassword;
	
	@Autowired
	private AuthService authService;

	@PreAuthorize("hasAuthority('ADMIN')") //comment out to debug 
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public Map<String, Boolean> login(@RequestParam String uid) {
		//System.out.println(uid);
		boolean successful = authService.login(uid, adminPassword);		
		Map<String, Boolean> map = new TreeMap<>();
		map.put("successful", successful);
		return map;
	}

}
