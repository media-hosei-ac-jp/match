package jp.co.sogeninc.semv2_be.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecrLimitData {
	public long settingId;
	
	public String uid;

    public Integer grade1Lower;
    
    public Integer grade1Upper;
    
    public Integer grade2Lower;

    public Integer grade2Upper;

    public Integer grade3Lower;

    public Integer grade3Upper;
    
    public Integer grade4Lower;

    public Integer grade4Upper;

}
