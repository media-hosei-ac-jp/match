package jp.co.sogeninc.semv2_be.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sogeninc.semv2_be.domain.MailTemplate;
import jp.co.sogeninc.semv2_be.repository.MailTemplateRepository;

@Service
@Transactional
public class MailTemplateService {
	@Autowired
	private MailTemplateRepository mailTemplateRepository;
	
	public MailTemplate get() {
		return mailTemplateRepository.findAll().get(0);
	}

}
