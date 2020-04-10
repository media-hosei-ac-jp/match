package jp.co.sogeninc.semv2_be.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * seminar DB参照モデル
 *
 *
 */
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Seminar {

	/**
	 * ゼミid
	 */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * ゼミ名称
     */
    @Column
    private String title;

    /**
     * 紹介
     */
    @Lob
    @Column
    private String description;

    /**
     * URL
     */
    @Column
    private String url;

    /**
     * ユーザid
     */
//    @Column(name="user_id")
//    private Long userId;
    @OneToOne
    @JoinColumn(name="owner_id")
    private User owner;

//	/**
//	 * 更新ユーザid
//	 */
//	@Column(name="update_user_Id")
//	private Long updateUserId;

//	/**
//	 * 作成ユーザid
//	 */
//	@Column(name="create_user_Id")
//	private Long createUserId;
    
//    @OneToOne(cascade=CascadeType.ALL)　//TODO コメントアウトすべきか（現在はファイルの更新時に旧ファイルが削除されない）
    @OneToOne
    @JoinColumn(name="file_id")
    private UploadFile file;

	/**
	 * 作成日時
	 */
	@Column
	private Date createDate;
	
	@Column
	private Date updateDate;

}
