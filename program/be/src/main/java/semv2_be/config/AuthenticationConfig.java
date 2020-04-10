package jp.co.sogeninc.semv2_be.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

import jp.co.sogeninc.semv2_be.service.AdminLoginUserDetailsService;

//認証処理設定
@Configuration
public class AuthenticationConfig extends GlobalAuthenticationConfigurerAdapter {
  //認証ユーザ取得サービス
  @Autowired
  private AdminLoginUserDetailsService adminUserDetailService;

  //認証処理設定
  public void init(AuthenticationManagerBuilder auth) throws Exception {
      auth
          //認証ユーザ取得サービスを指定
          .userDetailsService(adminUserDetailService);
  }
}
