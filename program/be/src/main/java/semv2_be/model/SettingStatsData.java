package jp.co.sogeninc.semv2_be.model;

import java.util.List;

import jp.co.sogeninc.semv2_be.domain.Recruitment;
import jp.co.sogeninc.semv2_be.domain.Setting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingStatsData {
	private Setting setting;
	private List<RecrStatsData> rsdl;
	
	//実人数
	private int[] applicantCounts;
	private int[] passCounts;
	private int[] confirmCounts;
	private int[] addPassCounts;
	private int[] addConfirmCounts;
}
