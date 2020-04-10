package jp.co.sogeninc.semv2_be.model;

import jp.co.sogeninc.semv2_be.domain.Recruitment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 統計データ用モデルクラス（管理者コントローラーで使用）
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecrStatsData {
	private Recruitment recr;
	private int[] appCounts;// = new int[5];
	private int[] passCounts;
	private int[] confirmCounts;
	private int[] addPassCounts;
	private int[] addConfirmCounts;
	
	public RecrStatsData(Recruitment recr, int[] appCounts) {
		super();
		this.recr = recr;
		this.appCounts = appCounts;
	}		
}
