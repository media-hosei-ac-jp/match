package jp.co.sogeninc.semv2_be.model;

import java.util.List;

import jp.co.sogeninc.semv2_be.domain.Application;
import jp.co.sogeninc.semv2_be.domain.Recruitment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectionData {
	public Recruitment recr;
	public List<Application> apps;
	public boolean additional;
}
