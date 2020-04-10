package jp.co.sogeninc.semv2_be.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * upload_file DB参照モデル
 *
 *
 */
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class UploadFile {
	@Id
	@GeneratedValue
	private Long id;

//	/**
//	 * 機能区分
//	 */
//	@Id
//	@Column(name="function_class")
//	private String functionClass;
//
//	/**
//	 * 機能ID
//	 */
//	@Column(name="function_id")
//	private Long functionId;

	/**
	 * ファイル
	 */
	@Column
	private String fileName;

	/**
	 * ファイル
	 */
	@Lob
	@Column
	@JsonProperty(access=Access.WRITE_ONLY)
	private String content;
	
	

//	/**
//	 * 更新ユーザid
//	 */
//	@Column(name="update_user_id")
//	private Long updateUserId;
//
//	/**
//	 * 更新日時
//	 */
//	@Column(name="update_date")
//	private Date updateDate;
//
//	/**
//	 * 作成ユーザid
//	 */
//	@Column(name="create_user_id")
//	private Long createUserId;
//
//	/**
//	 * 作成日時
//	 */
//	@Column(name="create_date")
//	private Date createDate;


}
