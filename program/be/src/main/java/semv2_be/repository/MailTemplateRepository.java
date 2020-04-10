package jp.co.sogeninc.semv2_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.sogeninc.semv2_be.domain.MailTemplate;

@Repository
public interface MailTemplateRepository  extends JpaRepository<MailTemplate, Long> {

}
