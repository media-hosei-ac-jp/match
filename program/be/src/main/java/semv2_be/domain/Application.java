package jp.co.sogeninc.semv2_be.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * application DB参照モデル
 *
 *
 */
@Entity
//@Table(uniqueConstraints={@UniqueConstraint(columnNames={"recruitment_id", "owner_id"})})
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Application {
//	public static enum ResultStatus { NEW, CHECK, GOKAKU, GOKAKU_ADD, FUGOKAKU, FUGOKAKU_ADD, SEMINAR, GOKAKU_TORIKESHI}
//	public static enum ResultInfo { GOKAKU, GOKAKU_ADD, FUGOKAKU, FUGOKAKU_ADD, GOKAKU_TORIKESHI }
//	public static enum NotificationStatus { MITSUCHI, TSUCHI, ADD_TSUCHI }
	
	/**
	 * 応募id
	 */
    @Id
    @GeneratedValue
    private Long id;

	/**
	 * ユーザid
	 */
    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;

	/**
	 * 募集id
	 */
    @OneToOne
    @JoinColumn(name="recruitment_id")
    private Recruitment recruitment;

//	/**
//	 * 応募状況
//	 */
//    @Enumerated(EnumType.STRING)
//    private ResultInfo status;
//
//	/**
//	 * 通知状況
//	 */
//    @Enumerated(EnumType.STRING)
//    private NotificationStatus notificationStatus;
    
    @Lob
    @Column
    private String pr;

	/**
	 * 更新日時
	 */
	@Column
	private Date updateDate;

	/**
	 * 作成日時
	 */
	@Column
	private Date createDate;

	/**
	 * コンストラクタ（キー）
	 * @param id
	 */
	public Application(long id){
		this.id = id;
	}
	
	@Column
	private boolean deleted = false;
	
	//学生がこのゼミに確定したか (nullは未定)
	@Column
	private Boolean confirmed;
	
	//合格したか (nullは未定)
	@Column
	private Boolean passed;
	
	//追加合格の合否結果
	//追加合格なら passed==true && addPassed == true
	//1次合格なら passed==true && addPassed == null
	@Column
	private Boolean addPassed;
	
	//通知済みかどうか TODO 未使用
	@Column
	private boolean notified;

}
