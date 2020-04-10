package jp.co.sogeninc.semv2_be.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sogeninc.semv2_be.domain.Setting;
import jp.co.sogeninc.semv2_be.repository.SettingRepository;

/**
 * 募集期間設定情報コントローラー用サービス
 *
 *
 */
@Service
@Transactional
public class SettingService {

	@Autowired
	private SettingRepository settingRepository;

//	/**
//	 * 設定1件取得（募集idより）
//	 *
//	 * @param id
//	 * @return User
//	 */
//	public Setting findByRecruitmentId(Long id) {
//		return settingRepository.findByRecruitmentId(id);
//	}
//
//	/**
//	 * 募集期間設定情報取得（募集用）1件
//	 *
//	 * @return
//	 */
//	public Setting findForRecDisp() {
//
//		List<Setting> list = settingRepository.findForRecDisp();
//		Setting setting = new Setting();
//		if (list != null && list.size() > 0){
//			setting = list.get(0);
//		}
//		return setting;
//
//	}
//
//	/**
//	 * 募集期間設定情報取得（応募用）1件
//	 *
//	 * @return
//	 */
//	public Setting findForAppDisp() {
//
//		List<Setting> list = settingRepository.findForAppDisp();
//		Setting setting = new Setting();
//		if (list != null && list.size() > 0){
//			setting = list.get(0);
//		}
//		return setting;
//	}


	/**
	 * 募集設定情報更新（新規作成を含む）
	 * @param setting
	 * @return
	 */
	public Setting save(Setting setting){
		return settingRepository.save(setting);
	}

	/**
	 * 募集期間取得（キーより）
	 * @param id
	 * @return
	 */
	public Setting findOne(Long id){
		return settingRepository.findOne(id);
	}

	/**
	 * 全募集期間情報取得
	 * @return
	 */
	public List<Setting> findAll(){
		List<Setting> l = settingRepository.findAll();
		Collections.sort(l, (s1, s2)->-s1.getId().compareTo(s2.getId()));
		return l;
	}

	public Setting getCurrentSetting() {
		return settingRepository.getCurrentSetting(new Date());
	}

}
