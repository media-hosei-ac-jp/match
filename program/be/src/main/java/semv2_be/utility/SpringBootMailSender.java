package jp.co.sogeninc.semv2_be.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * メール送信部品
 *
 */
@Component
public class SpringBootMailSender {
	// private static final Logger log =
	// LoggerFactory.getLogger(SpringBootMailSender.class);
	@Autowired
	private JavaMailSender javaMailSender;

//	/**
//	 * コンストラクタ
//	 *
//	 * @param javaMailSender
//	 */
//	@Autowired
//	public SpringBootMailSender(JavaMailSender javaMailSender) {
//		this.javaMailSender = javaMailSender;
//	}

	/**
	 * 簡易メール送信
	 *
	 * @param From
	 * @param To
	 * @param Cc
	 * @param Bcc
	 * @param ReplayTo
	 * @param subject
	 * @param content
	 * @return
	 */
	public SimpleMailMessage send(String From, String[] To, String[] Cc, String[] Bcc, String ReplayTo, String subject,
			String content) {

		SimpleMailMessage mailMessage = new SimpleMailMessage();

		// 宛先
		mailMessage.setTo(To);
		// 送信元
		mailMessage.setFrom(From);
		// Bccあれば
		if (Bcc != null){
			mailMessage.setBcc(Bcc);
		}
		// Ccあれば
		if (Cc != null){
			mailMessage.setCc(Cc);
		}
		// ReplayToあれば
		if (!ReplayTo.isEmpty()) {
			mailMessage.setReplyTo(ReplayTo);
		}
		// 件名
		mailMessage.setSubject(subject);
		// 本文
		mailMessage.setText(content);

		// 送信
		javaMailSender.send(mailMessage);

		return mailMessage;
	}

}
