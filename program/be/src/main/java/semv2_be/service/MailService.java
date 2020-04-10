package jp.co.sogeninc.semv2_be.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import jp.co.sogeninc.semv2_be.domain.Application;
import jp.co.sogeninc.semv2_be.domain.MailTemplate;
import jp.co.sogeninc.semv2_be.domain.MailTemplate.MailTemplateKey;
import jp.co.sogeninc.semv2_be.domain.MailTemplate.MailYamlKey;
import jp.co.sogeninc.semv2_be.domain.Recruitment;
import jp.co.sogeninc.semv2_be.domain.User;
import lombok.Getter;
import lombok.Setter;

@Service
@Transactional
@ConfigurationProperties(prefix="semv2.mail")
public class MailService {
	@Setter	
	private String from;
	@Getter
	@Setter	
	private String[] bcc;
	@Setter
	private boolean enabled;
	@Setter
	private String ignoreDomain;


	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private MailTemplateService mailTemplateService;
	@Autowired
	private UserService userService;    
	//    @Autowired
	//    private SettingsService settingsService;	
	@Autowired
	private ApplicationService applicationService;	

	public void send(String to, String subject, String content) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		String[] _to = to.split(",");
		for(int i=0; i<_to.length; i++) {
			_to[i] = _to[i].trim();
		}
		mailMessage.setTo(_to);
		mailMessage.setBcc(bcc);
		//		mailMessage.setReplyTo(reply);
		mailMessage.setFrom(from);
		mailMessage.setSubject(subject);
		mailMessage.setText(content);
		if(enabled&&!to.endsWith(ignoreDomain)) {
			javaMailSender.send(mailMessage); 
		}
		else {
			System.out.println("to: "+to);
			System.out.println("subject: "+subject);
			System.out.println("content: "+content);
		}
	}

	@SuppressWarnings("unchecked")
	private void send(String to, Map<String, String> repMap, MailTemplateKey templateKey) {
		MailTemplate mt = mailTemplateService.get();
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Map<String, String> m;
		try {
			m = mapper.readValue(mt.getMailTemplates().get(templateKey), Map.class);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException("failed to parse mail template yaml");
		}

		String subject = m.get(MailYamlKey.Subject.toString());
		String content = m.get(MailYamlKey.Content.toString());
		for(Entry<String, String> entry : repMap.entrySet()) {
			String value = entry.getValue();
			if(value==null) {
				value = "";
			}
			subject = subject.replace("${"+entry.getKey()+"}", value);
			content = content.replace("${"+entry.getKey()+"}", value);
		}
		send(to, subject, content);		
	}

	public void sendRecrCompleteMail(Recruitment recr) {
		String to = recr.getSeminar().getOwner().getEmail();

		TreeMap<String, String> repMap = new TreeMap<>(mailTemplateService.get().getMailVars());
		repMap.put("setting.title", recr.getSetting().getTitle());
		User owner = recr.getSeminar().getOwner();
		repMap.put("name", owner.getFamilyName()+" "+owner.getGivenName()); 
		repMap.put("grade1", recr.getGrade1().toString());
		repMap.put("grade2", recr.getGrade2().toString());
		repMap.put("grade3", recr.getGrade3().toString());
		repMap.put("grade4", recr.getGrade4().toString());
		repMap.put("info", recr.getInfo());

		send(to, repMap, MailTemplateKey.RecrComplete);
	}

	public void sendSelectCompleteMail(Recruitment recr, List<Application> apps, boolean isAdditional) {
		String to = recr.getSeminar().getOwner().getEmail();
				
		TreeMap<String, String> repMap = new TreeMap<>(mailTemplateService.get().getMailVars());
		repMap.put("setting.title", recr.getSetting().getTitle());
		User owner = recr.getSeminar().getOwner();
		repMap.put("name", owner.getFamilyName()+" "+owner.getGivenName()); 
		
		StringBuilder result = new StringBuilder();
		for(Application app : apps) {
			User user = app.getOwner();
			result.append(user.getUid()+", "+user.getFamilyName()+" "+user.getGivenName()+", "+
			(Boolean.TRUE.equals(app.getPassed())?"合格":"不合格")+"\n");			
		}

		repMap.put("result", result.toString()); 

		send(to, repMap, !isAdditional?MailTemplateKey.SelectComplete:MailTemplateKey.AddSelectComplete);
	}
	
	public void sendResultMail(Application app, boolean isAdditional) {
		String to = app.getOwner().getEmail();
		
		TreeMap<String, String> repMap = new TreeMap<>(mailTemplateService.get().getMailVars());
		repMap.put("setting.title", app.getRecruitment().getSetting().getTitle());
		repMap.put("sem.title", app.getRecruitment().getSeminar().getTitle());
		User owner = app.getOwner();
		repMap.put("name", owner.getFamilyName()+" "+owner.getGivenName()); 
		
		String result;
		if(Boolean.TRUE.equals(app.getPassed())) {
			result = "あなたはこのゼミに合格しました。\n"
					+ "合格ゼミの登録を行うまで配属は確定しないので注意してください。";
		}
		else {
			result = "あなたはこのゼミに不合格でした。";
		}		

		repMap.put("result", result);
		
		send(to, repMap, !isAdditional?MailTemplateKey.SendResult:MailTemplateKey.SendAddResult);
	}
	
	public void sendAppCompleteMail(Application app) {
		String to = app.getOwner().getEmail();
		
		TreeMap<String, String> repMap = new TreeMap<>(mailTemplateService.get().getMailVars());
		repMap.put("setting.title", app.getRecruitment().getSetting().getTitle());
		repMap.put("sem.title", app.getRecruitment().getSeminar().getTitle());
		User owner = app.getOwner();
		repMap.put("name", owner.getFamilyName()+" "+owner.getGivenName()); 
		repMap.put("pr", app.getPr());
		send(to, repMap, MailTemplateKey.AppComplete);
	}
	
	public void sendAppDeleteMail(Application app) {
		String to = app.getOwner().getEmail();
		
		TreeMap<String, String> repMap = new TreeMap<>(mailTemplateService.get().getMailVars());
		repMap.put("setting.title", app.getRecruitment().getSetting().getTitle());
		repMap.put("sem.title", app.getRecruitment().getSeminar().getTitle());
		User owner = app.getOwner();
		repMap.put("name", owner.getFamilyName()+" "+owner.getGivenName()); 
		repMap.put("pr", app.getPr());
		send(to, repMap, MailTemplateKey.AppDelete);
	}
	
	public void sendAppConfirmMail(Application app) {
		String to = app.getOwner().getEmail();
		
		TreeMap<String, String> repMap = new TreeMap<>(mailTemplateService.get().getMailVars());
		repMap.put("setting.title", app.getRecruitment().getSetting().getTitle());
		repMap.put("sem.title", app.getRecruitment().getSeminar().getTitle());
		User owner = app.getOwner();
		repMap.put("name", owner.getFamilyName()+" "+owner.getGivenName()); 
		repMap.put("pr", app.getPr());
		send(to, repMap, MailTemplateKey.AppConfirm);
	}
}
