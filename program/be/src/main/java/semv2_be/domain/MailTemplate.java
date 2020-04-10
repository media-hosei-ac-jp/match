package jp.co.sogeninc.semv2_be.domain;

import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class MailTemplate {
	//public enum MailTemplateKey { ApplyConfirm, Apply1ResultPassed, Apply1ResultFailed, Apply2ResultPassed, Apply2ResultFailed, ResultForTeacher1  };
	public enum MailTemplateKey { RecrComplete, AppComplete, AppDelete, SelectComplete, SendResult, AppConfirm, SendAddResult, AddSelectComplete }
	public enum MailVarKey { Department, Title, FrontendURL, Reply }
	public enum MailYamlKey { Subject, Content }
	
    @Id
    @GeneratedValue
    private Long id;
    
    @ElementCollection
    @CollectionTable
    @Lob
    @Column
    @MapKeyEnumerated(EnumType.STRING)
    private Map<MailTemplateKey, String> mailTemplates; //yaml形式
    
    @ElementCollection
    @CollectionTable(name="mailVars")
    @Column
    private Map<String, String> mailVars;
}
