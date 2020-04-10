package jp.co.sogeninc.semv2_be.model;

import java.util.List;

import jp.co.sogeninc.semv2_be.domain.Setting;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 合格者一覧印刷用
 * TODO 未使用
 *
 */
@Data
@NoArgsConstructor
public class PrintAppPassData {

//	/**
//	 * 合格情報（学年1）
//	 */
//	private List<ApplicationData> application1;
//
//	/**
//	 * 合格情報（学年2）
//	 */
//	private List<ApplicationData> application2;
//
//	/**
//	 * 合格情報（学年3）
//	 */
//	private List<ApplicationData> application3;
//
//	/**
//	 * 合格情報（学年4）
//	 */
//	private List<ApplicationData> application4;
//
//	/**
//	 * 募集情報
//	 */
//	private RecruitmentData recruitmentData;

	/**
	 * 募集期間設定情報
	 */
	private Setting setting;

//	/**
//	 * コンストラクタ
//	 *
//	 */
//	public PrintAppPassData(List<ApplicationData> appication1
//							, List<ApplicationData> appication2
//							, List<ApplicationData> appication3
//							, List<ApplicationData> appication4
//							, RecruitmentData recruitmentData
//							, Setting setting) {
//
//		this.application1 = appication1;
//		this.application2 = appication2;
//		this.application3 = appication3;
//		this.application4 = appication4;
//		this.recruitmentData = recruitmentData;
//		this.setting = setting;
//	}

}
