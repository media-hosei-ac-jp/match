package jp.co.sogeninc.semv2_be.api;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sogeninc.semv2_be.domain.Recruitment;
import jp.co.sogeninc.semv2_be.domain.Seminar;
import jp.co.sogeninc.semv2_be.domain.Setting;
import jp.co.sogeninc.semv2_be.domain.User;
import jp.co.sogeninc.semv2_be.domain.User.Role;
import jp.co.sogeninc.semv2_be.service.RecruitmentService;
import jp.co.sogeninc.semv2_be.service.SeminarService;
import jp.co.sogeninc.semv2_be.service.SettingService;
import jp.co.sogeninc.semv2_be.service.UserService;

/**
 * 募集期間設定関連コントローラー
 *
 *
 */
@RestController
@RequestMapping("api/setting")
public class SettingController {

	@Autowired
	private SettingService settingService;
	
	@Autowired
	private RecruitmentService recrService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SeminarService semService;

	/**
	 * 募集設定情報取得
	 *
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Setting> getAllSetting() {
		return settingService.findAll();
	}
	
	/**
	 * 募集設定情報新規作成
	 *
	 * @param settingData
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('ADMIN')")
	public Setting saveSetting(@RequestBody Setting setting) {
		Setting ret = settingService.save(setting);
		
		List<User> teachers = userService.findAllByRole(Role.TEACHER);
		for(User u : teachers) {
			Recruitment r = recrService.findByOwner(ret, u);
			if(r==null) {
				r = new Recruitment();
				r.setSetting(ret);
				Seminar s = semService.findByOwner(u);
				r.setSeminar(s);
				r.setCreateDate(new Date());
				recrService.save(r);
				
			}
		}
		return ret;
	}
	
}
