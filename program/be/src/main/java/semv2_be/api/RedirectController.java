package jp.co.sogeninc.semv2_be.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sogeninc.semv2_be.domain.User;
import lombok.Setter;

@RestController
@ConfigurationProperties(prefix="semv2")
public class RedirectController {
	@Setter
	private String frontendUrl;

	@RequestMapping("/")
	void login(HttpServletRequest req, HttpServletResponse res, @AuthenticationPrincipal User user) throws IOException, ServletException {


		if(user == null || user.getRoles().contains(User.Role.STRANGER))	{
			res.setContentType("text/html; charset=UTF-8");
			res.setCharacterEncoding("UTF-8");
			res.getWriter().println("ユーザとして登録されていないメールアドレスです。<br />");
			res.getWriter().println("登録された法政大学のGmailアドレスのみ利用可能です。<br />");
			res.getWriter().println("<a href=\"logout\">戻る（ログアウト）</a>");

			req.logout();

			HttpSession session = req.getSession(false);
			if(session != null) {
				session.invalidate();
			}
			return;
		}

		res.sendRedirect(frontendUrl);
	}

}
