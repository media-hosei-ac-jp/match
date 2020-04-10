package jp.co.sogeninc.semv2_be.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * setting DB参照モデル
 *
 *
 */
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Setting {
	//人数表示
	public enum DisplayType {ALWAYS, STEP} //逐次更新、段階更新

	/**
	 * 設定id
	 */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 設定名称
     */
    @Column
    private String title;

    /**
     * 募集開始期間
     */
    @Column
    private Date recruitmentStartDate;

    /**
     * 募集終了期間
     */
    @Column
    private Date recruitmentEndDate;

    /**
     * 応募開始期間
     */
    @Column
    private Date applicationStartDate;
    
    /**
     * 途中経過更新日時
     */
    @Column
    private Date dataRefreshDate;

    /**
     * 応募終了期間
     */
    @Column
    private Date applicationEndDate;

    /**
     * 選考開始期間
     */
    @Column
    private Date selectionStartDate;

    /**
     * 選考終了期間
     */
    @Column
    private Date selectionEndDate;

    /**
     * 合格登録開始期間
     */
    @Column
    private Date registrationStartDate;

    /**
     * 合格登録終了期間
     */
    @Column
    private Date registrationEndDate;

    /**
     * 追加合格者登録開始日（教員)
     */
    @Column
    private Date addSelectionStartDate;

    /**
     * 追加合格者登録終了日（教員)
     */
    @Column
    private Date addSelectionEndDate;

    /**
     * 追加合格先登録開始日（学生)
     */
    @Column
    private Date addRegistrationStartDate;

    /**
     * 追加合格先登録終了日（学生)
     */
    @Column
    private Date addRegistrationEndDate;

    /**
     * 応募状況表示機能
     */
    @Enumerated(EnumType.STRING)
    @Column
    private DisplayType displayType;

    /**
     * 最大応募可能数
     */
    @Column
    private Integer maximum;


//    /**
//     * デフォルト自己PR
//     */
//    @Column
//    private String defaultPr;

}
