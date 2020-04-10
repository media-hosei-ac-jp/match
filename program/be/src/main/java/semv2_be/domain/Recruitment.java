package jp.co.sogeninc.semv2_be.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import jp.co.sogeninc.semv2_be.model.RecrLimitData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * recruitment DB参照モデル
 *
 *
 */
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Recruitment {

	/**
	 * 募集id
	 */
    @Id
    @GeneratedValue
    private Long id;


//	/**
//	 * ゼミid
//	 */
//    @ManyToOne
//    @JoinColumn(name="owner_id")
//    private User owner;
    
    @ManyToOne
    @JoinColumn(name="seminar_id")
    private Seminar seminar;

	/**
	 * 設定id
	 */
    @OneToOne
    @JoinColumn(name="setting_id")
    private Setting setting;
    
    
    /**
     * 選考方法やレポートなど
     */
    @Lob
    @Column
    private String info;

    /** nullの時は制限なし **/
    @Column
    private Integer grade1;
    @Column
    private Integer grade1Lower;
    @Column
    private Integer grade1Upper;
    
    @Column
    private Integer grade2;
    @Column
    private Integer grade2Lower;
    @Column
    private Integer grade2Upper;
    
    @Column
    private Integer grade3;    
    @Column
    private Integer grade3Lower;
    @Column
    private Integer grade3Upper;
    
    @Column
    private Integer grade4;        
    @Column
    private Integer grade4Lower;
    @Column
    private Integer grade4Upper;
    
    
    /**
     * 合否を確定したか
     */
    @Column
    private boolean confirmed = false;
    
    /**
     * 追加合格の合否を確定したか
     */
    @Column
    private boolean addConfirmed = false;
    

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
	
	
	public boolean countValid() {
		return (grade1Lower==null||grade1Lower<=grade1) && (grade1Upper==null||grade1 <=grade1Upper)
				&& (grade2Lower==null||grade2Lower<=grade2) && (grade2Upper==null||grade2 <=grade2Upper)
				&& (grade3Lower==null||grade3Lower<=grade3) && (grade3Upper==null||grade3 <=grade3Upper)
				&& (grade4Lower==null||grade4Lower<=grade4) && (grade4Upper==null||grade4 <=grade4Upper);
	}
	
	public boolean registerationDateValid(Date now) {
		return now.after(setting.getRecruitmentStartDate()) && now.before(setting.getRecruitmentEndDate());
	}
	
	public boolean selectionDateValid(Date now) {
		return now.after(setting.getSelectionStartDate()) && now.before(setting.getSelectionEndDate());
	}
	
	public boolean addSelectionDateValid(Date now) {
		return now.after(setting.getAddSelectionStartDate()) && now.before(setting.getAddSelectionEndDate());
	}

	public void updateLimits(RecrLimitData rd) {
		this.grade1Lower = rd.grade1Lower;
		this.grade2Lower = rd.grade2Lower;
		this.grade3Lower = rd.grade3Lower;
		this.grade4Lower = rd.grade4Lower;
		this.grade1Upper = rd.grade1Upper;
		this.grade2Upper = rd.grade2Upper;
		this.grade3Upper = rd.grade3Upper;
		this.grade4Upper = rd.grade4Upper;		
	}

	public Recruitment(Recruitment r) {
		this(r.id, r.seminar, r.setting, r.info, r.grade1, r.grade1Lower, r.grade1Upper, r.grade2, r.grade2Lower, r.grade2Upper, r.grade3, r.grade3Lower, r.grade3Upper, r.grade4, r.grade4Lower, r.grade4Upper, r.confirmed, r.addConfirmed, r.updateDate, r.createDate);
	}

}
